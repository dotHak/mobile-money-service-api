package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.LegalDocument;
import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.repository.LegalDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LegalDocumentService implements GenericService<LegalDocument, Long> {

    private final LegalDocumentRepository repository;

    @Autowired
    public LegalDocumentService(LegalDocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<LegalDocument> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<LegalDocument> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public LegalDocument save(LegalDocument legalDocument) {
        return repository.save(legalDocument);
    }

    public List<LegalDocument> getAllByMerchant(Merchant merchant){
        return repository.findAllByMerchant(merchant);
    }

    public LegalDocument update(LegalDocument legalDocument, Long id){
        return repository.findById(id).map(oldDocument -> {
            oldDocument.setDocumentUrl(legalDocument.getDocumentUrl());
            return repository.save(oldDocument);
        }).orElseThrow(() -> new NotFoundException("No fingerprint found for id: " + id));
    }
}
