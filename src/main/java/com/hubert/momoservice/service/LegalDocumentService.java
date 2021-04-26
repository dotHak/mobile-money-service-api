package com.hubert.momoservice.service;

import com.hubert.momoservice.config.aws.FileStore;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.LegalDocument;
import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.repository.LegalDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class LegalDocumentService implements GenericService<LegalDocument, Long> {

    private static final String FOLDER_NAME = "legalDocuments";

    private final LegalDocumentRepository repository;

    private final FileStore fileStore;

    @Autowired
    public LegalDocumentService(LegalDocumentRepository repository, FileStore fileStore) {
        this.repository = repository;
        this.fileStore = fileStore;
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
        }).orElseThrow(() -> new NotFoundException("No document found for id: " + id));
    }

    public LegalDocument updateUrl(String url, Long id){
        return repository.findById(id).map(oldDocument -> {
            deleteFile(oldDocument.getDocumentUrl());
            oldDocument.setDocumentUrl(url);
            return repository.save(oldDocument);
        }).orElseThrow(() -> new NotFoundException("No document found for id: " + id));
    }

    public String uploadFile(MultipartFile multipartFile){
        return fileStore.uploadFile(multipartFile, FOLDER_NAME);
    }

    public byte[] getFile(LegalDocument legalDocument) {
        return fileStore.download(legalDocument.getDocumentUrl(), FOLDER_NAME);
    }

    @Async
    public void deleteFile(String fileUrl){
        fileStore.deleteFile(fileUrl,FOLDER_NAME);
    }
}
