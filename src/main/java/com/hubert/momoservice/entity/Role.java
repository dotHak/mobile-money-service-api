package com.hubert.momoservice.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", updatable = false)
    private short id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }

    public Role() {
    }

    public Role(short id, @NotNull RoleType name) {
        this.id = id;
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
