package com.hubert.momoservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "merchants")
public class Merchant extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private long id;
    @NotEmpty @NotNull
    private String name;
    @NotEmpty @NotNull @Email
    private String email;
    @NotEmpty @NotNull
    private String address;
    @NotEmpty @NotNull
    private String region;
    @NotEmpty @NotNull
    private String city;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "merchants_phone_numbers",
            joinColumns = @JoinColumn(name = "merchant_id"),
            inverseJoinColumns = @JoinColumn(name = "phone_number_id")
    )
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "user_id"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AppUser appUser;

    public Merchant() {
    }

    public Merchant(
            String name,
            String email,
            String address,
            String region,
            String city,
            AppUser appUser
    ) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.region = region;
        this.city = city;
        this.appUser = appUser;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @JsonIgnore
    public AppUser getUser() {
        return appUser;
    }

    public void setUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public PhoneNumber getDefaultPhoneNumber(){
        if(phoneNumbers.size() == 1){
            return phoneNumbers.get(0);
        }else {
            for (PhoneNumber number: phoneNumbers){
                if (number.getDefault()){
                    return number;
                }
            }
        }

        return null;
    }

    public void makeDefaultPhoneNumber(PhoneNumber phoneNumber){
        phoneNumbers.forEach(phoneNumber1 -> {
            phoneNumber1.setDefault(phoneNumber1.getNumber().equals(phoneNumber.getNumber()));
        });
    }
}
