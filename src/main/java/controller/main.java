package controller;

import model.*;
import service.GenericDAO;

import java.util.List;

public class main {
    public static void main(String[] args) {
        List<Room> r;
        GenericDAO<Room> RoomDAO = new GenericDAO<>(Room.class);
        r = RoomDAO.findAll();
        for (Room room : r) {
            System.out.println(room.getArea());
        }
    }
}
