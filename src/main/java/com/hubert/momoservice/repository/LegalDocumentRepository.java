package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.LegalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long> {
}
