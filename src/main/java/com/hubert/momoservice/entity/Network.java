package com.hubert.momoservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "networks")
public class Network {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @Column(
            nullable = false,
            unique = true
    )
    private String name;


    public Network() {
    }

    public Network(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
