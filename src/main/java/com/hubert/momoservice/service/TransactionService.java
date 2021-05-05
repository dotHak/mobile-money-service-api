package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.PhoneNumber;
import com.hubert.momoservice.entity.Transaction;
import com.hubert.momoservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements GenericService<Transaction, Long> {

  private final TransactionRepository repository;

  @Autowired
  public TransactionService(TransactionRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Transaction> getAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Transaction> getOne(Long id) {
    return repository.findById(id);
  }

  @Override
  public Transaction save(Transaction transaction) {
    return repository.save(transaction);
  }

  public List<Transaction> getReceived(PhoneNumber phoneNumber) {

    return repository.findAllByReceiverOrderByCreatedDateDesc(phoneNumber);
  }

  public List<Transaction> getSent(PhoneNumber phoneNumber) {

    return repository.findAllBySenderOrderByCreatedDateDesc(phoneNumber);
  }

  public List<Transaction> getAll(PhoneNumber sender, PhoneNumber receiver) {
    return repository.findAllBySenderOrReceiverOrderByCreatedDateDesc(sender, receiver);
  }
}
