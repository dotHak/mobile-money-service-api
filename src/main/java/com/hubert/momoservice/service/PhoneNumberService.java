package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.PhoneNumber;
import com.hubert.momoservice.repository.PhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneNumberService implements GenericService<PhoneNumber, Long> {

    private final PhoneNumberRepository repository;

    @Autowired
    public PhoneNumberService(PhoneNumberRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PhoneNumber> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PhoneNumber> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public PhoneNumber save(PhoneNumber phoneNumber) {
        return repository.save(phoneNumber);
    }
}
