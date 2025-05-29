package controller;

import model.Citizen;
import model.Household;
import service.CitizenDAO;
import service.HibernateUtil;
import service.HouseholdDAO;

import java.util.List;

import org.hibernate.Session;

public class HouseholdController {

    public List<Household> getAllHouseholds() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Household> households = null;
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            households = householdDAO.findAll();
            return households;
        }

    }

    public Household getHouseholdById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Household household = null;
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            household = householdDAO.findById(id);
            return household;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Household> findHouseholdsByOwnerName(String ownerName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Household> households = null;
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            households = householdDAO.findByOwnerName(ownerName);
            return households;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addHousehold(Household household) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            householdDAO.update(household);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHousehold(Integer householdId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            HouseholdDAO householdDAO = new HouseholdDAO(session);
            CitizenDAO citizenDAO = new CitizenDAO(session);

            Household household = householdDAO.findById(householdId);
            if (household == null) {
                System.err.println("Household not found with ID: " + householdId);
                return false;
            }

            Citizen newHead = citizenDAO.findById(newHeadId);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Citizen> members = null;
            CitizenDAO citizenDAO = new CitizenDAO(session);
            members = citizenDAO.findCitizensOfHousehold(householdId);
            return members;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}