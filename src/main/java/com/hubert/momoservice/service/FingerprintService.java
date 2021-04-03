package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.Fingerprint;
import com.hubert.momoservice.repository.FingerprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FingerprintService implements GenericService<Fingerprint, Long> {

    private final FingerprintRepository repository;

    @Autowired
    public FingerprintService(FingerprintRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Fingerprint> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Fingerprint> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public Fingerprint save(Fingerprint fingerprint) {
        return repository.save(fingerprint);
    }

    public Optional<Fingerprint> getFingerprintByUser(AppUser appUser){

        return  repository.findFingerprintByAppUser(appUser);
    }

    public Fingerprint update(Fingerprint fingerprint, Long id){

        return repository.findById(id).map(oldfinger -> {
            oldfinger.setImageUrl(fingerprint.getImageUrl());
            return repository.save(oldfinger);
        }).orElseThrow(() -> new NotFoundException("No fingerprint found for id: " + id));
    }
}
