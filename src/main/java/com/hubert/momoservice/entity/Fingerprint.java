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

@Setter
@Getter @NoArgsConstructor
@Entity
@Table(name = "fingerprints")
public class Fingerprint extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fingerprint_id")
    private long id;

    @NotEmpty
    @NotNull
    private String imageUrl;

    @JsonIgnore
    @NotNull
    private byte[] byteData;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_details_id",
            nullable = false,
            referencedColumnName = "detail_id"
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserDetail UserDetail;

    public Fingerprint(@NotEmpty @NotNull String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Fingerprint(
            @NotEmpty @NotNull String imageUrl,
            @NotNull byte[] byteData,
            UserDetail userDetail) {
        this.imageUrl = imageUrl;
        this.byteData = byteData;
        UserDetail = userDetail;
    }
}
