package service;

import model.Room;
import org.hibernate.Session;

import java.util.List;
import java.util.UUID;

public class RoomDAO extends GenericDAO<Room> {
    public RoomDAO(Session session) {
        super(Room.class, session);
    }

    public List<Room> findRoomNotCharge(UUID chargeId) {
        String hql = """
        FROM Room r
        WHERE r.id NOT IN (
            SELECT ph.room.id
            FROM PaidHousehold ph
            WHERE ph.residentCharge.id = :chargeId
        )
    """;

        return session.createQuery(hql, Room.class)
                .setParameter("chargeId", chargeId)
                .getResultList();
    }

}
