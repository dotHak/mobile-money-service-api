package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity()
@Table(name = "user_details")
public class UserDetail extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private long id;

    @NotEmpty @NotNull
    private String firstName;
    @NotEmpty @NotNull
    private String lastName;

    private String middleName;
    @NotEmpty @NotNull
    private String houseNumber;
    @NotEmpty @NotNull
    private String region;
    @NotEmpty @NotNull
    private String city;
    @NotEmpty @NotNull
    private String town;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "user_id"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AppUser appUser;

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
            AppUser appUser
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.houseNumber = houseNumber;
        this.region = region;
        this.city = city;
        this.town = town;
        this.appUser = appUser;
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

    @JsonIgnore
    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
