package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.dto.TransactionDto;
import com.hubert.momoservice.entity.*;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.PhoneNumberService;
import com.hubert.momoservice.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Transaction", description = "The transaction API for the CRUD operations")
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

  private final TransactionService transactionService;
  private final AppUserService userService;
  private final PhoneNumberService phoneNumberService;

  @Autowired
  public TransactionController(TransactionService transactionService, AppUserService userService,
      PhoneNumberService phoneNumberService) {
    this.transactionService = transactionService;
    this.userService = userService;
    this.phoneNumberService = phoneNumberService;
  }

  @GetMapping
  public List<Transaction> getTransactions(Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    List<Transaction> transactionList = new ArrayList<>();
    if (user.isPresent()) {
      var appUser = user.get();
      appUser.getPhoneNumbers().forEach(phoneNumber -> {
        var list = transactionService.getAll(phoneNumber, phoneNumber);
        transactionList.addAll(list);
      });

    } else {
      throw new NotFoundException("No user found");
    }

    return transactionList;
  }

  @GetMapping("/sent")
  public List<Transaction> getTransactionsSent(Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    List<Transaction> transactionList = new ArrayList<>();
    if (user.isPresent()) {
      var appUser = user.get();
      appUser.getPhoneNumbers().forEach(phoneNumber -> {
        var list = transactionService.getSent(phoneNumber);
        transactionList.addAll(list);
      });

    } else {
      throw new NotFoundException("No user found");
    }

    return transactionList;
  }

  @GetMapping("/received")
  public List<Transaction> getTransactionsReceived(Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    List<Transaction> transactionList = new ArrayList<>();
    if (user.isPresent()) {
      var appUser = user.get();
      appUser.getPhoneNumbers().forEach(phoneNumber -> {
        var list = transactionService.getReceived(phoneNumber);
        transactionList.addAll(list);
      });

    } else {
      throw new NotFoundException("No user found");
    }

    return transactionList;
  }


  @GetMapping("/{id}")
  public Transaction getOne(@PathVariable Long id) {
    return transactionService
        .getOne(id)
        .orElseThrow(() -> new NotFoundException("Transaction not found for id: " + id));
  }

  @PostMapping
  public Transaction addNewTransaction
      (@Valid @RequestBody TransactionDto transaction, Principal principal) {
    if (transaction.getEmail() == null && transaction.getReceiver() == null) {
      throw new BadRequestException("Email or Sender phone number must be present");
    }

    var userOptional = userService.findUserEmail(principal.getName());
    if (userOptional.isEmpty()) {
      throw new NotFoundException("User not found for logged in user");
    }

    var appUser = userOptional.get();

    Optional<PhoneNumber> sender;
    PhoneNumber receiver;

    if (transaction.getSender() != null) {
      sender = phoneNumberService.getPhoneNumberByNumber(transaction.getSender().getNumber());
    } else {
      sender = phoneNumberService
          .getPhoneNumberByNumber(appUser.getDefaultPhoneNumber().getNumber());
    }

    if (transaction.getEmail() != null) {
      Optional<AppUser> transferUser = userService.findUserEmail(transaction.getEmail());
      if (transferUser.isEmpty()) {
        throw new NotFoundException(String.format("No user found for the email %s.",
            transaction.getEmail()));
      }

      receiver = transferUser.get().getDefaultPhoneNumber();
    } else {
      receiver = phoneNumberService.save(transaction.getReceiver());
    }

    if (sender.isEmpty()) {
      throw new NotFoundException("No phone number found for the current user");
    }

    Transaction transactionSave = new Transaction(sender.get(), receiver, transaction.getPrice(),
        new Status((short) 4, StatusType.PENDING));

    return transactionService.save(transactionSave);

  }

}
