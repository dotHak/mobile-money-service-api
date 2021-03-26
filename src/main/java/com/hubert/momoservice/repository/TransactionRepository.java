package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.PhoneNumber;
import com.hubert.momoservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    public List<Transaction> findAllByReceiver_IdOrderByCreatedDateDesc(long id);

    public List<Transaction> findAllByReceiverOrderByCreatedDateDesc(PhoneNumber phoneNumber);

    public List<Transaction> findAllBySender_IdOrderByCreatedDateDesc(long id);

    public List<Transaction> findAllBySenderOrderByCreatedDateDesc(PhoneNumber phoneNumber);

}
