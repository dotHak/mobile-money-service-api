package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends Auditable implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id", updatable = false)
  private long id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(
      name = "sender_id",
      referencedColumnName = "phone_id",
      nullable = false
  )
  @NotNull
  private PhoneNumber sender;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(
      name = "receiver_id",
      referencedColumnName = "phone_id",
      nullable = false
  )
  @NotNull
  private PhoneNumber receiver;

  @NotNull
  private double price;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(
      name = "status_id",
      referencedColumnName = "status_id",
      nullable = false
  )
  private Status status;

  public Transaction(PhoneNumber sender, PhoneNumber receiver, double price, Status status) {
    this.sender = sender;
    this.receiver = receiver;
    this.price = price;
    this.status = status;
  }
}
