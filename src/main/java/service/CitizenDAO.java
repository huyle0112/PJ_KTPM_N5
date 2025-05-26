package service;

import model.Citizen;
import model.Household;
import model.Residence;
import model.ResidenceId;

import org.hibernate.Session;

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

    public List<Citizen> findCitizensOfHousehold(Integer householdId) {
        String hql = "SELECT c FROM Residence r JOIN r.citizenid c WHERE r.householdid.id = :hid";
        return session.createQuery(hql, Citizen.class)
                .setParameter("hid", householdId)
                .getResultList();
    }
    
    public void setHousehold(Citizen citizen, Household household, String relationshipToOwner) {
        if (citizen == null || citizen.getId() == null) {
            throw new IllegalArgumentException("Citizen object and its ID must not be null.");
        }
        if (household == null || household.getId() == null) {
            throw new IllegalArgumentException("Household object and its ID must not be null.");
        }
        
        ResidenceId residenceId = new ResidenceId();
        residenceId.setCitizenid(citizen.getId());
        residenceId.setHouseholdid(household.getId());

        Residence residence = session.get(Residence.class, residenceId);

        if (residence == null) {
            // Citizen is not yet associated with this household, create new Residence record
            residence = new Residence();
            residence.setId(residenceId); // Set the populated composite ID
            residence.setCitizenid(citizen);     // Set the Citizen entity association
            residence.setHouseholdid(household); // Set the Household entity association
            residence.setRelationshiptoowner(relationshipToOwner);
            session.persist(residence);
        } else {
            // Citizen is already associated, update the relationship if different
            if (relationshipToOwner == null && residence.getRelationshiptoowner() != null ||
                relationshipToOwner != null && !relationshipToOwner.equals(residence.getRelationshiptoowner())) {
                residence.setRelationshiptoowner(relationshipToOwner);
                session.merge(residence); // Use merge for detached or already persistent entities
            }
        }
    }


}
