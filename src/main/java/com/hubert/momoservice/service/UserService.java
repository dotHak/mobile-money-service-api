package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.repository.AppUserRepository;
import com.hubert.momoservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements GenericService<AppUser, Long>{

    private final AppUserRepository appUserRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserService(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    @Override
    public Optional<AppUser> getOne(Long id) {
        return appUserRepository.findById(id);
    }

    @Override
    public AppUser save(AppUser appUser) {
        return appUserRepository.save(appUser);
    }

}
