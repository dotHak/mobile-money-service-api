package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements GenericService<AppUser, Long> {

  private final AppUserRepository appUserRepository;

  @Autowired
  public UserService(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
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
