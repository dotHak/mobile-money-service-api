package com.hubert.momoservice.dto;

import com.hubert.momoservice.entity.PhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class TransactionDto {

  private PhoneNumber receiver;
  private PhoneNumber sender;
  @NotNull
  private double price;
  @Email
  private String email;

}
