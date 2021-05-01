package com.hubert.momoservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "statuses")
@Getter
@Setter
@NoArgsConstructor
public class Status{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private short id;

    @Enumerated(EnumType.STRING)
    private StatusType name;

    public Status(StatusType name) {
        this.name = name;
    }

    public Status(short id, StatusType name) {
        this.id = id;
        this.name = name;
    }
}
