package com.hubert.momoservice.entity;

import com.hubert.momoservice.config.auditing.Auditable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "legal_documents")
public class LegalDocument extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "TEXT"
    )
    private String documentUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "merchant_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Merchant merchant;

    public LegalDocument() {
    }

    public LegalDocument(String documentUrl) {
        this.documentUrl = documentUrl;
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

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
}
