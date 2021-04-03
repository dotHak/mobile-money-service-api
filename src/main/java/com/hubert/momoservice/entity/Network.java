package com.hubert.momoservice.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "networks")
public class Network implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "network_id")
    private short id;

    @Enumerated(EnumType.STRING)
    private NetworkType name;


    public Network() {
    }

    public Network(NetworkType name) {
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public NetworkType getName() {
        return name;
    }

    public void setName(NetworkType name) {
        this.name = name;
    }
}
