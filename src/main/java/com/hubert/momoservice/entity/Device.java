package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
public class Device  extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private long id;

    @NotEmpty @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "merchant_id",
            nullable = false,
            referencedColumnName = "merchant_id"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Merchant merchant;

    public Device(String name) {
        this.name = name;
    }

    public Device(String name, Merchant merchant) {
        this.name = name;
        this.merchant = merchant;
    }
}
