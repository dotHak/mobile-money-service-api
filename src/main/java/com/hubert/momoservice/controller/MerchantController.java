package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.entity.Role;
import com.hubert.momoservice.entity.RoleType;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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

    if (user.isPresent()) {
      merchant.setAppUser(user.get());
      AppUser appUser = user.get();
      appUser.getRoles().add(new Role((short) 2, RoleType.MERCHANT));
      userService.save(appUser);
    }
    ;

    try {
      return merchantService.save(merchant);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException(
          e.getCause().getMessage() + ", (name) = (" + merchant.getName() + ") already exists");
    }
  }

  @PutMapping("/{id}")
  public Merchant updateMerchant
      (@RequestBody Merchant merchant, @PathVariable Long id) {
    return merchantService.update(merchant, id);
  }
}
