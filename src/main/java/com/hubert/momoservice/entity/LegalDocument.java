package com.hubert.momoservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "legal_documents")
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

    public LegalDocument() {
    }

    public LegalDocument(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public LegalDocument(@NotEmpty @NotNull String documentUrl, Merchant merchant) {
        this.documentUrl = documentUrl;
        this.merchant = merchant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    @JsonIgnore
    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
