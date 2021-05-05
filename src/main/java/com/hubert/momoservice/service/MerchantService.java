package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.entity.AppUser;
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
    return repository.saveAndFlush(merchant);
  }

  public List<Merchant> getMerChantsByUser(AppUser appUser) {
    return repository.findAllByAppUser(appUser);
  }

  public Merchant update(Merchant merchant, Long id) {
    return repository
        .findById(id).map(oldMerchant -> {
          String name = merchant.getName() == null ?
              oldMerchant.getName() : merchant.getName();
          String email = merchant.getEmail() == null ?
              oldMerchant.getEmail() : merchant.getEmail();
          String address = merchant.getAddress() == null ?
              oldMerchant.getAddress() : merchant.getAddress();
          String region = merchant.getRegion() == null ?
              oldMerchant.getRegion() : merchant.getRegion();
          String city = merchant.getCity() == null ?
              oldMerchant.getCity() : merchant.getCity();

          oldMerchant.setName(name);
          oldMerchant.setEmail(email);
          oldMerchant.setAddress(address);
          oldMerchant.setRegion(region);
          oldMerchant.setCity(city);

          return repository.save(oldMerchant);
        })
        .orElseThrow(() -> new BadRequestException("No merchant found for id: " + id));
  }

  public Optional<Merchant> getByIdAndUser(Long id, AppUser appUser) {
    return repository.findMerchantByAppUserAndId(appUser, id);
  }
}
