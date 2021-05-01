package com.hubert.momoservice.dto;

import com.hubert.momoservice.entity.PhoneNumber;

record TransactionDto(PhoneNumber receiver, PhoneNumber sender, double price, String email) {
}
