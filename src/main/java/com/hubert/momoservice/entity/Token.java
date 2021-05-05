package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
public class Token implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "token_id")
  private long id;

  @NotEmpty
  @NotNull
  private String apiToken;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      referencedColumnName = "user_id"
  )
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private AppUser appUser;

  public Token(String apiToken) {
    this.apiToken = apiToken;
  }

  public Token(String apiToken, AppUser appUser) {
    this.apiToken = apiToken;
    this.appUser = appUser;
  }
}
