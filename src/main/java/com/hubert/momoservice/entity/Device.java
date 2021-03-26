package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "devices")
public class Device  extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "merchant_id",
            nullable = false,
            referencedColumnName = "id"
    )
    private Merchant merchant;


    public Device() {
    }

    public Device(String name) {
        this.name = name;
    }

    public Device(String name, Merchant merchant) {
        this.name = name;
        this.merchant = merchant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
