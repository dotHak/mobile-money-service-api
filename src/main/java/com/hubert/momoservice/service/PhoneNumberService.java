package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.Network;
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

  public void saveAll(Iterable<PhoneNumber> phoneNumbers) {
    repository.saveAll(phoneNumbers);
  }

  public PhoneNumber update(PhoneNumber phoneNumber, Long id) {
    return repository.findById(id).map(oldNumber -> {
      String number = phoneNumber.getNumber() == null ?
          oldNumber.getNumber() : phoneNumber.getNumber();

      boolean isDefault = phoneNumber.isDefault() == oldNumber.isDefault() ?
          oldNumber.isDefault() : phoneNumber.isDefault();

      Network network = phoneNumber.getNetwork() == null ?
          oldNumber.getNetwork() : phoneNumber.getNetwork();

      oldNumber.setNumber(number);
      oldNumber.setDefault(isDefault);
      oldNumber.setNetwork(network);

      return repository.save(oldNumber);
    }).orElseThrow(() -> new NotFoundException("Phone number not found for id: " + id));

  }

  public Optional<PhoneNumber> getPhoneNumberByNumber(String number) {
    return repository.findPhoneNumberByNumber(number);
  }

}
