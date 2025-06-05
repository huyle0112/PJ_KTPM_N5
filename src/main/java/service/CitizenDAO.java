package service;

import model.Citizen;
import model.Household;
import model.Residence;
import model.ResidenceId;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CitizenDAO extends GenericDAO<Citizen> {
    public CitizenDAO(Session session) {
        super(Citizen.class, session);
    }

    public List<Citizen> findByName(String name) {
        String hql = "FROM Citizen c WHERE lower(c.fullname) LIKE lower(:name)";
        return session.createQuery(hql, Citizen.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    public List<Citizen> findCitizensOfHousehold(int householdId) {
        Household household = session.get(Household.class, householdId);
        String hql = "SELECT c, r.relationshiptoowner FROM Residence r JOIN r.citizenid c WHERE r.householdid = :hid";
        List<Object[]> results = session.createQuery(hql, Object[].class)
                .setParameter("hid", household)
                .getResultList();
        List<Citizen> citizens = new java.util.ArrayList<>();
        for (Object[] row : results) {
            Citizen citizen = (Citizen) row[0];
            String relationship = (String) row[1];
            citizen.setRelationshipToOwner(relationship);
            citizens.add(citizen);
        }
        return citizens;
    }

    @Override
    public void delete(Citizen citizen) {
        Transaction tx = session.beginTransaction();
        try{
            int citizenId = citizen.getId();
            String hqlCheckHead  = "FROM Household h WHERE h.head.id = :cid";
            List<Household> households = session.createQuery(hqlCheckHead , Household.class)
                    .setParameter("cid", citizenId)
                    .getResultList();

            if (!households.isEmpty()) {
                throw new IllegalStateException("Không thể xoá công dân vì là chủ hộ.");
            }

            String hqlFindResidences = "FROM Residence r WHERE r.id.citizenid = :cid";
            List<Residence> residences = session.createQuery(hqlFindResidences, Residence.class)
                    .setParameter("cid", citizenId)
                    .getResultList();
            for (Residence r : residences) {
                session.remove(r);
            }

            session.remove(session.contains(citizen) ? citizen : session.merge(citizen));
            tx.commit();
        }catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    public boolean addCitizen(Citizen citizen, Integer householdId, String relation, boolean isHead, Citizen.ResidencyStatus status) {
        Transaction tx = session.beginTransaction();
        try{
            save(citizen);
            session.flush();
            if (status == Citizen.ResidencyStatus.Away || status == Citizen.ResidencyStatus.Permanent){
                if (householdId == null) {
                    tx.rollback();
                    throw new IllegalArgumentException("Vui lòng nhập ID hộ khẩu!");
                }

                String _relation = isHead ? "Head" : relation;
                Household household = session.get(Household.class, householdId);

                if (household == null) {
                    tx.rollback();
                    throw new IllegalArgumentException("Không tìm thấy hộ khẩu!");
                }

                if (isHead) {
                    if (household.getHead() != null) {
                        tx.rollback();
                        throw new IllegalStateException("Hộ này đã có chủ hộ. Vui lòng kiểm tra lại!");
                    }
                    household.setHead(citizen);
                    session.merge(household);
                }

                ResidenceId residenceId = new ResidenceId(citizen.getId(), householdId);
                Residence residence = new Residence();
                residence.setId(residenceId);
                residence.setCitizenid(citizen);
                residence.setHouseholdid(household);
                residence.setRelationshiptoowner(_relation);

                session.persist(residence);
            }
            tx.commit();
            return true;
        }
        catch (Exception ex) {
            tx.rollback();
            throw ex;
        }
    }

    @Override
    public void save(Citizen citizen) {
        session.save(citizen);
    }
}
