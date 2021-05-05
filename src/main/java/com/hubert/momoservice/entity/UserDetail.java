package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@Entity()
@Table(name = "user_details")
public class UserDetail extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "detail_id")
  private long id;

  @NotEmpty
  @NotNull
  private String firstName;
  @NotEmpty
  @NotNull
  private String lastName;

  private String middleName;
  @NotEmpty
  @NotNull
  private String houseNumber;
  @NotEmpty
  @NotNull
  private String region;
  @NotEmpty
  @NotNull
  private String city;
  @NotEmpty
  @NotNull
  private String town;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      referencedColumnName = "user_id"
  )
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private AppUser appUser;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinTable(
      name = "fingerprints",
      joinColumns = @JoinColumn(name = "user_details_id"),
      inverseJoinColumns = @JoinColumn(name = "fingerprint_id")
  )
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private Fingerprint fingerprint;

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
      @NotEmpty @NotNull String firstName,
      @NotEmpty @NotNull String lastName,
      String middleName,
      @NotEmpty @NotNull String houseNumber,
      @NotEmpty @NotNull String region,
      @NotEmpty @NotNull String city,
      @NotEmpty @NotNull String town,
      AppUser appUser,
      Fingerprint fingerprint
  ) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.middleName = middleName;
    this.houseNumber = houseNumber;
    this.region = region;
    this.city = city;
    this.town = town;
    this.appUser = appUser;
    this.fingerprint = fingerprint;
  }
}
