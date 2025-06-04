package model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "paid_room")
public class PaidHousehold {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paid_room_id_seq")
    @SequenceGenerator(name = "paid_room_id_seq", sequenceName = "paid_room_id_seq", allocationSize = 1)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomid")
    private Room room;

    @Column(name = "fullname", length = 50)
    private String fullname;

    @Column(name = "paid_date")
    private Instant paidDate;

    @Column(name = "amount_paid")
    private Integer amountPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id")
    private ResidentCharge residentCharge;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Room getRoomid() {
        return room;
    }

    public void setRoomid(Room roomid) {
        this.room = roomid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Instant getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Instant paidDate) {
        this.paidDate = paidDate;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public ResidentCharge getCharge() {
        return residentCharge;
    }

    public void setCharge(ResidentCharge charge) {
        this.residentCharge = charge;
    }

}