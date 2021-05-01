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
@Table(name = "legal_documents")
@Getter
@Setter
@NoArgsConstructor
public class LegalDocument extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private long id;

    @NotEmpty
    @NotNull
    private String documentUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "merchant_id",
            referencedColumnName = "merchant_id",
            nullable = false
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Merchant merchant;


    public LegalDocument(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public LegalDocument(@NotEmpty @NotNull String documentUrl, Merchant merchant) {
        this.documentUrl = documentUrl;
        this.merchant = merchant;
    }

}
