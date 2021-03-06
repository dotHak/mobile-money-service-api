package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity()
@Table(name = "app_users")
@NoArgsConstructor
@Getter
@Setter
public class AppUser extends Auditable implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private long id;

  @Email
  @NotEmpty
  @NotNull
  private String email;

  @NotEmpty
  @NotNull
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  private Boolean locked = false;
  private Boolean enabled = false;


  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private Set<Role> roles = new HashSet<>();

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "users_phone_numbers",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "phone_number_id")
  )
  private List<PhoneNumber> phoneNumbers = new ArrayList<>();


  public AppUser(@Email @NotEmpty String email, @NotEmpty String password) {
    this.email = email;
    this.password = password;
  }

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    roles.forEach(role -> {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
    });

    return grantedAuthorities;
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
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
    phoneNumbers.forEach(phoneNumber1 -> phoneNumber1
        .setDefault(phoneNumber1.getNumber().equals(phoneNumber.getNumber())));
  }

}
