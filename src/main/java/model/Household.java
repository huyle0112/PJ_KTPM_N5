package model;

import jakarta.persistence.*;

@Entity
@Table(name = "household")
public class Household {
    @Id
    @Column(name = "householdid", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headid")
    private Citizen head;

    @Column(name = "address", length = 200)
    private String address;

    public Household() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Citizen getHead() {
        return head;
    }

    public void setHead(Citizen head) {
        this.head = head;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}