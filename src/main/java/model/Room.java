package model;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "roomid", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_id_seq")
    @SequenceGenerator(name = "room_id_seq", sequenceName = "room_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "roomnumber", length = 5)
    private String roomnumber;

    @Column(name = "area")
    private Integer area;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Room(Integer id, String roomnumber, Integer area) {
        this.id = id;
        this.roomnumber = roomnumber;
        this.area = area;
    }

    public Room() {
    }
}