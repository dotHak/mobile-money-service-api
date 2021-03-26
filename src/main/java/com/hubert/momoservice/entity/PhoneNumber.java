package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber  extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String number;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "network_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Network network;

    public PhoneNumber() {
    }

    public PhoneNumber(String number, Network network) {
        this.number = number;
        this.network = network;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

}
