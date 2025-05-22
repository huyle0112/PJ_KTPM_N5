package model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "paid_household")
public class PaidHousehold {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "householdid")
    private Household householdid;

    @Column(name = "fullname", length = 50)
    private String fullname;

    @Column(name = "paid_date")
    private Instant paidDate;

    @Column(name = "amount_paid")
    private Integer amountPaid;

    public PaidHousehold() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Household getHouseholdid() {
        return householdid;
    }

    public void setHouseholdid(Household householdid) {
        this.householdid = householdid;
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

}