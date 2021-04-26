package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;
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
public class AppUser extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Email
    @NotEmpty @NotNull
    private String email;

    @NotEmpty @NotNull
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "fingerprints",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "fingerprint_id")
    )
    @JsonIgnore
    private Fingerprint fingerprint;

    public AppUser() {
    }

    public AppUser(@Email @NotEmpty String email, @NotEmpty String password) {
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        roles.forEach( role -> {
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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


    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }
}
