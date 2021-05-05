package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.PhoneNumber;
import com.hubert.momoservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  public List<Transaction> findAllByReceiverOrderByCreatedDateDesc(PhoneNumber phoneNumber);

  public List<Transaction> findAllBySenderOrderByCreatedDateDesc(PhoneNumber phoneNumber);

  public List<Transaction> findAllBySenderOrReceiverOrderByCreatedDateDesc
      (@NotNull PhoneNumber sender, @NotNull PhoneNumber receiver);

}
