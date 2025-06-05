package service;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
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
        List<Citizen> citizens = new ArrayList<>();
        for (Object[] row : results) {
            Citizen citizen = (Citizen) row[0];
            String relationship = (String) row[1];
            citizen.setRelationshipToOwner(relationship);
            citizens.add(citizen);
        }
        return citizens;
    }

    @Override
    public void save(Citizen citizen) {
        session.save(citizen);
    }


    @Override
    public void delete(Citizen citizen) {
        Transaction tx = session.beginTransaction();
        try {
            int citizenId = citizen.getId();

            String hqlCheckHead = "FROM Household h WHERE h.head.id = :cid";
            boolean isHead = !session.createQuery(hqlCheckHead, Household.class)
                    .setParameter("cid", citizenId)
                    .getResultList()
                    .isEmpty();
            if (isHead) {
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
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }

    public boolean addCitizen(Citizen citizen, Integer householdId, String relation, boolean isHead, Citizen.ResidencyStatus status) {
        Transaction tx = session.beginTransaction();
        try {
            if (citizen.getRoomid() != null) {
                Room room = session.get(Room.class, citizen.getRoomid().getId());
                citizen.setRoomid(room);
            }
            save(citizen);
            session.flush();

            if (needsResidence(status)) {
                Household household = validateAndGetHousehold(householdId);

                String _relation = isHead ? "Head" : relation;

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
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        }
    }

    public void updateCitizen(Citizen citizen, Integer householdId, String relation, boolean isHead, Citizen.ResidencyStatus status) {
        Transaction tx = session.beginTransaction();
        try {
            if (citizen.getRoomid() != null) {
                Room room = session.get(Room.class, citizen.getRoomid().getId());
                citizen.setRoomid(room);
            }
            session.merge(citizen);

            if (needsResidence(status)) {
                Household household = validateAndGetHousehold(householdId);
                String _relation = isHead ? "Head" : relation;

                handleHeadStatus(citizen, household, isHead);

                ResidenceId residenceId = new ResidenceId(citizen.getId(), householdId);
                Residence residence = session.get(Residence.class, residenceId);
                if (residence == null) {
                    residence = new Residence();
                    residence.setId(residenceId);
                    residence.setCitizenid(citizen);
                    residence.setHouseholdid(household);
                }
                residence.setRelationshiptoowner(_relation);
                session.merge(residence);
            }
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        }
    }


    private boolean needsResidence(Citizen.ResidencyStatus status) {
        return status == Citizen.ResidencyStatus.Away || status == Citizen.ResidencyStatus.Permanent;
    }

    private Household validateAndGetHousehold(Integer householdId) {
        if (householdId == null) {
            throw new IllegalArgumentException("Vui lòng nhập ID hộ khẩu!");
        }
        Household household = session.get(Household.class, householdId);
        if (household == null) {
            throw new IllegalArgumentException("Không tìm thấy hộ khẩu!");
        }
        return household;
    }

    private void handleHeadStatus(Citizen citizen, Household household, boolean isHead) {
        if (isHead) {
            Citizen currentHead = household.getHead();
            if (currentHead != null && !currentHead.getId().equals(citizen.getId())) {
                throw new IllegalStateException("Hộ này đã có chủ hộ khác. Vui lòng kiểm tra lại!");
            }
            household.setHead(citizen);
            session.merge(household);
        } else {
            if (household.getHead() != null && household.getHead().getId().equals(citizen.getId())) {
                household.setHead(null);
                session.merge(household);
            }
        }
    }
}
