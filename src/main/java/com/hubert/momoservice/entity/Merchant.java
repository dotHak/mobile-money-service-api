package com.hubert.momoservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
public class Merchant extends Auditable implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "merchant_id")
  private long id;
  @NotEmpty
  @NotNull
  private String name;
  @NotEmpty
  @NotNull
  @Email
  private String email;
  @NotEmpty
  @NotNull
  private String address;
  @NotEmpty
  @NotNull
  private String region;
  @NotEmpty
  @NotNull
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

  public PhoneNumber getDefaultPhoneNumber() {
    if (phoneNumbers.size() == 1) {
      return phoneNumbers.get(0);
    } else {
      for (PhoneNumber number : phoneNumbers) {
        if (number.isDefault()) {
          return number;
        }
      }
    }

    return null;
  }

  public void makeDefaultPhoneNumber(PhoneNumber phoneNumber) {
    phoneNumbers.forEach(phoneNumber1 -> {
      phoneNumber1.setDefault(phoneNumber1.getNumber().equals(phoneNumber.getNumber()));
    });
  }
}
