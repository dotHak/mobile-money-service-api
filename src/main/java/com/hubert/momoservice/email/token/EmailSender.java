package com.hubert.momoservice.email.token;

public interface EmailSender {

  void send(String to, String email);
}
