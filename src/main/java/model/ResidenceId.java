package model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResidenceId implements Serializable {
    private static final long serialVersionUID = -9102170542319250933L;
    @Column(name = "citizenid", nullable = false)
    private Integer citizenid;

    @Column(name = "householdid", nullable = false)
    private Integer householdid;

    public Integer getCitizenid() {
        return citizenid;
    }

    public void setCitizenid(Integer citizenid) {
        this.citizenid = citizenid;
    }

    public Integer getHouseholdid() {
        return householdid;
    }

    public void setHouseholdid(Integer householdid) {
        this.householdid = householdid;
    }

    public ResidenceId(Integer citizenid, Integer householdid) {
        this.citizenid = citizenid;
        this.householdid = householdid;
    }

    public ResidenceId() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ResidenceId entity = (ResidenceId) o;
        return Objects.equals(this.householdid, entity.householdid) &&
                Objects.equals(this.citizenid, entity.citizenid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(householdid, citizenid);
    }

}