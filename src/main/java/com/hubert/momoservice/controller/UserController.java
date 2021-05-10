package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.RegistrationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Tag(name="User", description = "The user API for the CRUD operations")
@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

  private final RegistrationService registrationService;

  private final AppUserService appUserService;

  @Autowired
  public UserController(RegistrationService registrationService,
      AppUserService appUserService) {
    this.registrationService = registrationService;
    this.appUserService = appUserService;
  }

  @PostMapping("/signup")
  public ResponseEntity<Map<String, String>>
  register(@Valid @RequestBody AppUser appUser) {
    HashMap<String, String> map = new HashMap<>();
    map.put("token", registrationService.register(appUser));

    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @GetMapping(path = "/signup/confirm")
  public ResponseEntity<String>
  confirm(@RequestParam("token") String token) {

    return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
  }

  @GetMapping
  public AppUser getUser(Principal principal) {
    return appUserService.findUserEmail(principal.getName())
        .orElseThrow(() -> new NotFoundException("Not user found for the current account."));
  }
}
