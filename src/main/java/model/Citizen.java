package model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "citizen")
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "citizenid", nullable = false)
    private Integer id;

    @Column(name = "fullname", length = 100)
    private String fullname;

    @Column(name = "dateofbirth")
    private LocalDate dateofbirth;

    @Column(name = "placeofbirth", length = 100)
    private String placeofbirth;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "nationalid", length = 12)
    private String nationalid;

    public enum ResidencyStatus {
        Unknown, Temporary, Away, Permanent
    }

    @Column(name = "residencystatus")
    @Enumerated(EnumType.STRING)
    private ResidencyStatus residencyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomid")
    private Room roomid;

    public Citizen() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getPlaceofbirth() {
        return placeofbirth;
    }

    public void setPlaceofbirth(String placeofbirth) {
        this.placeofbirth = placeofbirth;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public Room getRoomid() {
        return roomid;
    }

    public void setRoomid(Room roomid) {
        this.roomid = roomid;
    }

    public ResidencyStatus getResidencyStatus() {
        return residencyStatus;
    }

    public void setResidencyStatus(ResidencyStatus residencyStatus) {
        this.residencyStatus = residencyStatus;
    }
}