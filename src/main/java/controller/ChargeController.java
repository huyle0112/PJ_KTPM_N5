package controller;

import model.PaidHousehold;
import model.Room;
import org.hibernate.Session;
import service.HibernateUtil;
import service.PaidHouseholdDAO;
import service.RoomDAO;

import java.util.List;
import java.util.UUID;

public class ChargeController {
    private final Session session;
    private final PaidHouseholdDAO paidHouseholdDAO;
    private final RoomDAO roomDAO;

    public ChargeController(Session session){
        this.session = session;
        this.paidHouseholdDAO = new PaidHouseholdDAO(session);
        this.roomDAO = new RoomDAO(session);
    }

    public void charge(PaidHousehold paidHousehold){
        paidHouseholdDAO.save(paidHousehold);
    }

    public List<Room> getRoomsNotCharge(UUID chargeId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return roomDAO.findRoomNotCharge(chargeId);
        }
    }

    public List<Room> getRooms(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return roomDAO.findAll();
        }
    }
}
