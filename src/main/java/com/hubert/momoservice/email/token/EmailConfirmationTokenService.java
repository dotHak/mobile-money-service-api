package com.hubert.momoservice.email.token;

import com.hubert.momoservice.entity.AppUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailConfirmationTokenService {

  public EmailConfirmationTokenService(
      EmailConfirmationTokenRepository confirmationTokenRepository) {
    this.confirmationTokenRepository = confirmationTokenRepository;
  }

  private final EmailConfirmationTokenRepository confirmationTokenRepository;

  public void saveConfirmationToken(EmailConfirmationToken token) {
    confirmationTokenRepository.save(token);
  }

  public Optional<EmailConfirmationToken> getToken(String token) {
    return confirmationTokenRepository.findByToken(token);
  }

  public int setConfirmedAt(String token) {
    return confirmationTokenRepository.updateConfirmedAt(
        token, LocalDateTime.now());
  }

  public Optional<EmailConfirmationToken> findLatestToken(AppUser appUser) {
    return
        confirmationTokenRepository
            .findEmailConfirmationTokenByAppUserOrderByConfirmedAtDesc(appUser);
  }
}