package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerchantService implements GenericService<Merchant, Long> {

    private final MerchantRepository repository;

    @Autowired
    public MerchantService(MerchantRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Merchant> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Merchant> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public Merchant save(Merchant merchant) {
        return repository.save(merchant);
    }
}
