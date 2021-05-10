package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.entity.Role;
import com.hubert.momoservice.entity.RoleType;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.MerchantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name="Merchant", description = "The merchants API for the CRUD operations")
@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {

  private final MerchantService merchantService;
  private final AppUserService userService;

  @Autowired
  public MerchantController(MerchantService merchantService, AppUserService userService) {
    this.merchantService = merchantService;
    this.userService = userService;
  }

  @GetMapping
  public List<Merchant> getMerchants(Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    if (user.isPresent()) {
      return merchantService
          .getMerChantsByUser(user.get());
    } else {
      throw new NotFoundException("No user found");
    }
  }


  @GetMapping("/{id}")
  public Merchant getOne(@PathVariable Long id) {
    return merchantService
        .getOne(id)
        .orElseThrow(() -> new NotFoundException("User details not found for id: " + id));
  }

  @PostMapping
  public Merchant addNewMerchant
      (@Valid @RequestBody Merchant merchant, Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    user.ifPresent(merchant::setAppUser);

    try {
      return merchantService.save(merchant);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException(
          e.getCause().getMessage() + ", (name) = (" + merchant.getName() + ") already exists");
    }
  }

  @PostMapping("/addMerchantRole")
  public ResponseEntity<String> addMerchantRole(Principal principal){
    var user = userService.findUserEmail(principal.getName());
  if(user.isEmpty()){
    throw new NotFoundException("No user found for the current user token");
  }
    AppUser appUser = user.get();
    appUser.getRoles().add(new Role((short) 2, RoleType.MERCHANT));
    userService.save(appUser);

    return new ResponseEntity<>("Success", HttpStatus.OK);
  }

  @DeleteMapping("/removeMerchantRole")
  public ResponseEntity<String> removeMerchantRole(Principal principal){
    var user = userService.findUserEmail(principal.getName());

    if(user.isEmpty()){
      throw new NotFoundException("No user found for the current user token");
    }

    AppUser appUser = user.get();

    appUser.getRoles().removeIf(role -> role.getName() == RoleType.MERCHANT);
    userService.save(appUser);

    return new ResponseEntity<>("Success", HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public Merchant updateMerchant
      (@RequestBody Merchant merchant, @PathVariable Long id) {
    return merchantService.update(merchant, id);
  }
}
