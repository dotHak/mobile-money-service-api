package com.hubert.momoservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "networks")
@Getter
@Setter
@NoArgsConstructor
public class Network implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "network_id")
  private short id;

  @Enumerated(EnumType.STRING)
  private NetworkType name;

  public Network(NetworkType name) {
    this.name = name;
  }
}
