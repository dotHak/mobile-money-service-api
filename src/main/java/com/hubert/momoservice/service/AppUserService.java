package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.email.token.EmailConfirmationToken;
import com.hubert.momoservice.email.token.EmailConfirmationTokenService;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.Role;
import com.hubert.momoservice.entity.RoleType;
import com.hubert.momoservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService implements UserDetailsService {

  private final static String USER_NOT_FOUND_MSG =
      "user with email %s not found";

  private final AppUserRepository appUserRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final EmailConfirmationTokenService emailConfirmationTokenService;

  @Autowired
  public AppUserService(AppUserRepository appUserRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      EmailConfirmationTokenService emailConfirmationTokenService) {
    this.appUserRepository = appUserRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.emailConfirmationTokenService = emailConfirmationTokenService;
  }

  @Override
  public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    return appUserRepository.findUserByEmail(email)
        .orElseThrow(() ->
            new UsernameNotFoundException(
                String.format(USER_NOT_FOUND_MSG, email)));
  }

  public String addNewUser(AppUser appUser) {
    Optional<AppUser> user = appUserRepository.findUserByEmail(appUser.getEmail());
    boolean userExists = user.isPresent();
    if (userExists) {

      if (user.get().getEnabled()) {
        throw new BadRequestException(
            "Email " + appUser.getEmail() + " already exists"
        );
      }

      String token = UUID.randomUUID().toString();

      EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
          token,
          LocalDateTime.now(),
          LocalDateTime.now().plusMinutes(15),
          user.get()
      );

      emailConfirmationTokenService.saveConfirmationToken(emailConfirmationToken);

      return token;

    }

    String encodedPassword = bCryptPasswordEncoder
        .encode(appUser.getPassword());

    appUser.setPassword(encodedPassword);
    appUser.getRoles().add(new Role((short) (RoleType.USER.ordinal() + 1), RoleType.USER));

    appUser = appUserRepository.save(appUser);

    String token = UUID.randomUUID().toString();

    EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
        token,
        LocalDateTime.now(),
        LocalDateTime.now().plusMinutes(15),
        appUser
    );

    emailConfirmationTokenService.saveConfirmationToken(emailConfirmationToken);

    return token;
  }

  public int enableAppUser(String email) {
    return appUserRepository.enableAppUser(email);
  }

  public Optional<AppUser> findUserEmail(String email) {
    return appUserRepository.findUserByEmail(email);
  }

  public AppUser save(AppUser appUser) {
    return appUserRepository.save(appUser);
  }
}