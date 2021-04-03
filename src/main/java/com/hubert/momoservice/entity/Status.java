package com.hubert.momoservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
public class Status{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private short id;

    @Enumerated(EnumType.STRING)
    private StatusType name;

    public Status() {
    }

    public Status(StatusType name) {
        this.name = name;
    }

    public Status(short id, StatusType name) {
        this.id = id;
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public StatusType getName() {
        return name;
    }

    public void setName(StatusType name) {
        this.name = name;
    }
}
