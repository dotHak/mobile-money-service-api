package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.Token;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Tag(name="Token", description = "The token API for the CRUD operations")
@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController {

  private final TokenService tokenService;
  private final AppUserService userService;

  @Autowired
  public TokenController(TokenService tokenService, AppUserService userService) {
    this.tokenService = tokenService;
    this.userService = userService;
  }

  @Operation(summary = "Find all token", description = "Returns all tokens")
  @GetMapping
  public List<Token> getToken(Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    if (user.isPresent()) {
      return tokenService
          .getTokensByUser(user.get());
    } else {
      throw new NotFoundException("No user found");
    }
  }

}
