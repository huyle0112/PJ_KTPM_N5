package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resident_charge")
public class ResidentCharge {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "charge_id", nullable = false)
    private UUID id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "type_of_charge", length = 50)
    private String typeOfCharge;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "money")
    private Integer money;

    @Column(name = "sum_charge")
    private Integer sumCharge;

    @Column(name = "households_paid_count")
    private Integer householdsPaidCount;

    @Column(name = "in_complete")
    private Boolean inComplete;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfCharge() {
        return typeOfCharge;
    }

    public void setTypeOfCharge(String typeOfCharge) {
        this.typeOfCharge = typeOfCharge;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getSumCharge() {
        return sumCharge;
    }

    public void setSumCharge(Integer sumCharge) {
        this.sumCharge = sumCharge;
    }

    public Integer getHouseholdsPaidCount() {
        return householdsPaidCount;
    }

    public void setHouseholdsPaidCount(Integer householdsPaidCount) {
        this.householdsPaidCount = householdsPaidCount;
    }

    public Boolean getInComplete() {
        return inComplete;
    }

    public void setInComplete(Boolean inComplete) {
        this.inComplete = inComplete;
    }

}