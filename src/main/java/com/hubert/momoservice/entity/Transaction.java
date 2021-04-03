package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "transactions")
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

    public Transaction() {
    }

    public Transaction(PhoneNumber sender, PhoneNumber receiver, double price, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.price = price;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PhoneNumber getSender() {
        return sender;
    }

    public void setSender(PhoneNumber sender) {
        this.sender = sender;
    }

    public PhoneNumber getReceiver() {
        return receiver;
    }

    public void setReceiver(PhoneNumber receiver) {
        this.receiver = receiver;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
