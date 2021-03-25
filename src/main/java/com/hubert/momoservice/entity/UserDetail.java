package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import java.io.Serializable;

@Entity()
@Table(name = "user_details")
public class UserDetail extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;
    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;
    @Column(
            name = "middle_name",
            columnDefinition = "TEXT"
    )
    private String middleName;
    @Column(
            name = "house_number",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String houseNumber;
    @Column(
            name = "region",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String region;
    @Column(
            name = "city",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String city;
    @Column(
            name = "town",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String town;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private User user;

    public UserDetail() {
    }

    public UserDetail(
            String firstName,
            String lastName,
            String middleName,
            String houseNumber,
            String region,
            String city,
            String town
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.houseNumber = houseNumber;
        this.region = region;
        this.city = city;
        this.town = town;
    }

    public UserDetail(
            String firstName,
            String lastName,
            String middleName,
            String houseNumber,
            String region,
            String city,
            String town,
            User user
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.houseNumber = houseNumber;
        this.region = region;
        this.city = city;
        this.town = town;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
