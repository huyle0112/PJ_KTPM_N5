package model;

import jakarta.persistence.*;

@Entity
@Table(name = "residence")
public class Residence {
    @EmbeddedId
    private ResidenceId id;

    @MapsId("citizenid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "citizenid", nullable = false)
    private Citizen citizenid;

    @MapsId("householdid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "householdid", nullable = false)
    private Household householdid;

    @Column(name = "relationshiptoowner", length = 50)
    private String relationshiptoowner;

    public Residence() {
    }

    public ResidenceId getId() {
        return id;
    }

    public void setId(ResidenceId id) {
        this.id = id;
    }

    public Citizen getCitizenid() {
        return citizenid;
    }

    public void setCitizenid(Citizen citizenid) {
        this.citizenid = citizenid;
    }

    public Household getHouseholdid() {
        return householdid;
    }

    public void setHouseholdid(Household householdid) {
        this.householdid = householdid;
    }

    public String getRelationshiptoowner() {
        return relationshiptoowner;
    }

    public void setRelationshiptoowner(String relationshiptoowner) {
        this.relationshiptoowner = relationshiptoowner;
    }

}