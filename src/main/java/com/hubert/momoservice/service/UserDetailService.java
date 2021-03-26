package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.UserDetail;
import com.hubert.momoservice.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements GenericService<UserDetail, Long>{

    private final UserDetailRepository repository;

    @Autowired
    public UserDetailService(UserDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDetail> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserDetail> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public UserDetail save(UserDetail userDetail) {
        return repository.save(userDetail);
    }
}
