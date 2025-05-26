package controller;

import model.Citizen;
import model.Household;
import service.CitizenDAO;
import service.HouseholdDAO;

import java.util.List;

import org.hibernate.Session;

public class HouseholdController {

    private final Session session;

    public HouseholdController(Session session) {
        this.session = session;
    }

    public List<Household> getAllHouseholds() {
        List<Household> households = null;
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            households = householdDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return households;
    }

    public Household getHouseholdById(Integer id) {
        Household household = null;
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            household = householdDAO.findById(id); // Assuming GenericDAO has findById()
        } catch (Exception e) {
            e.printStackTrace();
        }
        return household;
    }

    public List<Household> findHouseholdsByOwnerName(String ownerName) {
        List<Household> households = null;
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            households = householdDAO.findByOwnerName(ownerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return households;
    }

    public boolean addHousehold(Household household) {
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            if (household.getHead() == null || household.getHead().getId() == null) {
                System.err.println("Household head is required.");
                return false;
            }

            householdDAO.save(household); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateHousehold(Household household) {
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            householdDAO.update(household);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHousehold(Integer householdId) {
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            Household household = householdDAO.findById(householdId);
            if (household != null) {
                householdDAO.delete(household);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeHouseholdHead(Integer householdId, Integer newHeadId) {
        try {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            CitizenDAO citizenDAO = new CitizenDAO(session); // Assuming CitizenDAO exists

            Household household = householdDAO.findById(householdId);
            if (household == null) {
                System.err.println("Household not found with ID: " + householdId);
                return false;
            }

            Citizen newHead = citizenDAO.findById(newHeadId); // Assuming CitizenDAO has findById
            if (newHead == null) {
                System.err.println("New head citizen not found with ID: " + newHeadId);
                return false;
            }

            household.setHead(newHead);
            householdDAO.update(household);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Citizen> getMembersByHouseholdId(Integer householdId) {
        List<Citizen> members = null;
        try {
            CitizenDAO citizenDAO = new CitizenDAO(session);
            members = citizenDAO.findCitizensOfHousehold(householdId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return members;
    }
}