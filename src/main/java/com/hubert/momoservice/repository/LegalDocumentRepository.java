package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.LegalDocument;
import com.hubert.momoservice.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long> {
    public List<LegalDocument> findAllByMerchant(Merchant merchant);
}
