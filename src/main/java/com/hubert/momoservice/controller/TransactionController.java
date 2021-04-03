package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.*;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.PhoneNumberService;
import com.hubert.momoservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AppUserService userService;
    private final PhoneNumberService phoneNumberService;

    @Autowired
    public TransactionController(TransactionService transactionService, AppUserService userService, PhoneNumberService phoneNumberService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.phoneNumberService = phoneNumberService;
    }

    @GetMapping
    public List<Transaction> getTransactions(Principal principal){
        var user = userService.findUserEmail(principal.getName());

        List<Transaction> transactionList = new ArrayList<>();
        if(user.isPresent()){
            var appUser = user.get();
            appUser.getPhoneNumbers().forEach(phoneNumber -> {
                var list = transactionService.getAll(phoneNumber, phoneNumber);
                transactionList.addAll(list);
            });

        }else {
            throw new NotFoundException("No user found");
        }

        return transactionList;
    }

    @GetMapping("/sent")
    public List<Transaction> getTransactionsSent(Principal principal){
        var user = userService.findUserEmail(principal.getName());

        List<Transaction> transactionList = new ArrayList<>();
        if(user.isPresent()){
            var appUser = user.get();
            appUser.getPhoneNumbers().forEach(phoneNumber -> {
                var list = transactionService.getSent(phoneNumber);
                transactionList.addAll(list);
            });

        }else {
            throw new NotFoundException("No user found");
        }

        return transactionList;
    }

    @GetMapping("/received")
    public List<Transaction> getTransactionsReceived(Principal principal){
        var user = userService.findUserEmail(principal.getName());

        List<Transaction> transactionList = new ArrayList<>();
        if(user.isPresent()){
            var appUser = user.get();
            appUser.getPhoneNumbers().forEach(phoneNumber -> {
                var list = transactionService.getReceived(phoneNumber);
                transactionList.addAll(list);
            });

        }else {
            throw new NotFoundException("No user found");
        }

        return transactionList;
    }


    @GetMapping("/{id}")
    public Transaction getOne(@PathVariable Long id){
        return transactionService
                .getOne(id)
                .orElseThrow( () -> new NotFoundException("Transaction not found for id: " + id));
    }

    @PostMapping
    public Transaction addNewTransaction
            (@Valid @RequestBody Transaction transaction, Principal principal){
        var user = userService.findUserEmail(principal.getName());
        var phone1 = phoneNumberService.getPhoneNumberByNumber(transaction.getSender().getNumber());
        var phone2 = phoneNumberService.getPhoneNumberByNumber(transaction.getReceiver().getNumber());

        if(phone1.isPresent()){
            transaction.setSender(phone1.get());
        }else {
            try{
               var number =  phoneNumberService.save(transaction.getSender());
               if(user.isPresent()){
                   AppUser appUser = user.get();
                   appUser.getPhoneNumbers().add(number);
                   userService.save(appUser);
               }
            }catch (Exception e){
               throw new BadRequestException("Could not save sender" + e.getCause().getLocalizedMessage());
            }
        }

        if(phone2.isPresent()){
            transaction.setSender(phone2.get());
        }else {
            try{
                phoneNumberService.save(transaction.getReceiver());
            }catch (Exception e){
                throw new BadRequestException("Could not save sender" + e.getCause().getLocalizedMessage());
            }
        }


        transaction.setStatus(new Status((short) 4,StatusType.PENDING));

        return transactionService.save(transaction);

    }

}
