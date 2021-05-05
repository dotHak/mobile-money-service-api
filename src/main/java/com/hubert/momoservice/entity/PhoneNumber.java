package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "phone_numbers")
@Getter
@Setter
@NoArgsConstructor
public class PhoneNumber extends Auditable implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "phone_id", updatable = false)
  private long id;

  @NotEmpty
  @NotNull
  @Size(min = 10, max = 10, message = "Phone number must be 10 numbers.")
  private String number;

  private boolean isDefault = false;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(
      name = "network_id",
      nullable = false,
      referencedColumnName = "network_id"
  )
  private Network network;

  public PhoneNumber(String number, Network network) {
    this.number = number;
    this.network = network;
  }
}
