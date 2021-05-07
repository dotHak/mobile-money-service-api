package com.hubert.momoservice.dto;

import com.hubert.momoservice.entity.PhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public record TransactionDto(PhoneNumber receiver, PhoneNumber sender, @NotNull double price,
                              @Email String email) {

}
