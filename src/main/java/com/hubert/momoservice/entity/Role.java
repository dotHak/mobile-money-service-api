package com.hubert.momoservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id", updatable = false)
  private short id;

  @Enumerated(EnumType.STRING)
  @NotNull
  private RoleType name;

  public Role(RoleType name) {
    this.name = name;
  }

  public Role(short id, @NotNull RoleType name) {
    this.id = id;
    this.name = name;
  }
}
