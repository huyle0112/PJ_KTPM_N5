package service;

import model.Household;
import model.Residence;
import model.ResidenceId;
import model.Citizen;
import org.hibernate.Session;

public class ResidentDAO extends GenericDAO<Residence> {
    public ResidentDAO(Session session) {
        super(Residence.class, session);
    }

    public void addCitizenToHousehold(int citizenId, int householdId, String relation) {
        Household household = session.get(Household.class, householdId);
        Citizen citizen = session.get(Citizen.class, citizenId);
        if (household == null) throw new IllegalArgumentException("Không tìm thấy hộ khẩu!");
        if (citizen == null) throw new IllegalArgumentException("Không tìm thấy công dân!");
        ResidenceId rid = new ResidenceId(citizenId, householdId);
        Residence residence = new Residence();
        residence.setId(rid);
        residence.setCitizenid(citizen);
        residence.setHouseholdid(household);
        residence.setRelationshiptoowner(relation);
        session.beginTransaction();
        session.persist(residence);
        session.getTransaction().commit();
    }

    public void updateRelationship(int citizenId, int householdId, String newRelation) {
        ResidenceId rid = new ResidenceId(citizenId, householdId);
        session.beginTransaction();
        Residence residence = session.get(Residence.class, rid);
        if (residence == null) throw new IllegalArgumentException("Không tìm thấy quan hệ cư trú!");
        residence.setRelationshiptoowner(newRelation);
        session.merge(residence);
        session.getTransaction().commit();
    }

    public void removeCitizenFromHousehold(int citizenId, int householdId) {
        ResidenceId rid = new ResidenceId(citizenId, householdId);
        session.beginTransaction();
        Residence residence = session.get(Residence.class, rid);
        if (residence == null) throw new IllegalArgumentException("Không tìm thấy quan hệ cư trú!");
        session.remove(residence);
        session.getTransaction().commit();
    }

    public void changeHouseholdHead(int newHeadCitizenId, int householdId) {
        session.beginTransaction();
        Household household = session.get(Household.class, householdId);
        Citizen newHead = session.get(Citizen.class, newHeadCitizenId);
        if (household == null) throw new IllegalArgumentException("Không tìm thấy hộ khẩu!");
        if (newHead == null) throw new IllegalArgumentException("Không tìm thấy công dân!");
        // Set new head
        household.setHead(newHead);
        session.merge(household);
        // Update all relationships in Residence
        String hql = "FROM Residence r WHERE r.householdid.id = :hid";
        @SuppressWarnings("unchecked")
        java.util.List<Residence> residences = session.createQuery(hql)
            .setParameter("hid", householdId)
            .getResultList();
        for (Residence r : residences) {
            if (r.getCitizenid().getId().equals(newHeadCitizenId)) {
                r.setRelationshiptoowner("Head");
            } else {
                r.setRelationshiptoowner("");
            }
            session.merge(r);
        }
        session.getTransaction().commit();
    }
} 