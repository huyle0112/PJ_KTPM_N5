package model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "paid_room")
public class PaidRoom {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomid")
    private Room roomid;

    @Column(name = "fullname", length = 50)
    private String fullname;

    @Column(name = "paid_date")
    private Instant paidDate;

    @Column(name = "amount_paid")
    private Integer amountPaid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_id")
    private ResidentCharge charge;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Room getRoomid() {
        return roomid;
    }

    public void setRoomid(Room roomid) {
        this.roomid = roomid;
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
        return charge;
    }

    public void setCharge(ResidentCharge charge) {
        this.charge = charge;
    }

}