package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.PhoneNumber;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.MerchantService;
import com.hubert.momoservice.service.PhoneNumberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Tag(name="PhoneNumber", description = "The phone number API for the CRUD operations")
@RestController
@RequestMapping("/api/v1/phoneNumbers")
public class PhoneNumberController {

  private final PhoneNumberService phoneNumberService;
  private final AppUserService userService;
  private final MerchantService merchantService;

  @Autowired
  public PhoneNumberController(
      PhoneNumberService phoneNumberService,
      AppUserService userService,
      MerchantService merchantService) {

    this.phoneNumberService = phoneNumberService;
    this.userService = userService;
    this.merchantService = merchantService;
  }

  @GetMapping("/user")
  public List<PhoneNumber> getUserPhoneNumbers(Principal principal) {
    var user = userService.findUserEmail(principal.getName());

    if (user.isPresent()) {
      return user.get().getPhoneNumbers();
    } else {
      throw new NotFoundException("No user found");
    }
  }

  @GetMapping("/merchant/{merchantId}")
  public List<PhoneNumber> getMerchantPhoneNumbers(Principal principal,
      @PathVariable Long merchantId) {
    var user = userService.findUserEmail(principal.getName());

    if (user.isPresent()) {
      var merch = merchantService.getByIdAndUser(merchantId, user.get());
      if (merch.isPresent()) {
        return merch.get().getPhoneNumbers();
      }
    } else {
      throw new NotFoundException("No user found");
    }

    return new ArrayList<>();
  }


  @GetMapping("/{id}")
  public PhoneNumber getOne(@PathVariable Long id) {
    return phoneNumberService
        .getOne(id)
        .orElseThrow(() -> new NotFoundException("Phone number not found for id: " + id));
  }

  @PostMapping("/user")
  public PhoneNumber addUserNewPhoneNumber
      (@Valid @RequestBody PhoneNumber phoneNumber, Principal principal) {
    var user = userService.findUserEmail(principal.getName());
    var temp = phoneNumber;
    try {
      temp = phoneNumberService.save(phoneNumber);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException(
          e.getCause().getMessage() + ", (number) = (" + phoneNumber.getNumber()
              + ") already exists");
    }

    if (user.isPresent()) {
      AppUser appUser = user.get();
      if (appUser.getPhoneNumbers().isEmpty()) {
        temp.setDefault(true);
        temp = phoneNumberService.save(phoneNumber);
      } else if (phoneNumber.isDefault()) {
        appUser.getPhoneNumbers().forEach(phoneNumber1 -> {
          phoneNumber1.setDefault(false);
        });

        phoneNumberService.saveAll(appUser.getPhoneNumbers());
      }

      appUser.getPhoneNumbers().add(temp);
      userService.save(appUser);
    }

    return temp;
  }


  @PostMapping("/merchant/{merchantId}")
  public PhoneNumber addMerchantNewPhoneNumber
      (@Valid @RequestBody PhoneNumber phoneNumber,
          Principal principal, @PathVariable Long merchantId) {
    var user = userService.findUserEmail(principal.getName());

    PhoneNumber temp = phoneNumber;
    try {
      temp = phoneNumberService.save(phoneNumber);
    } catch (DataIntegrityViolationException e) {
      throw new BadRequestException(
          e.getCause().getMessage() + ", (number) = (" + phoneNumber.getNumber()
              + ") already exists");
    }

    if (user.isPresent()) {
      var merch = merchantService.getByIdAndUser(merchantId, user.get());
      if (merch.isPresent()) {
        var merchant = merch.get();
        if (merchant.getPhoneNumbers().isEmpty()) {
          temp.setDefault(true);
          temp = phoneNumberService.save(temp);
        } else if (phoneNumber.isDefault()) {
          merchant.getPhoneNumbers().forEach(phoneNumber1 -> {
            phoneNumber1.setDefault(false);
          });

          phoneNumberService.saveAll(merchant.getPhoneNumbers());
        }

        merchant.getPhoneNumbers().add(temp);
        merchantService.save(merchant);
      }
    }

    return temp;
  }

  @PutMapping("/user/{id}")
  public PhoneNumber updateUserPhoneNumber(
      @RequestBody PhoneNumber phoneNumber,
      @PathVariable Long id, Principal principal
  ) {
    if (phoneNumber.isDefault()) {
      var user = userService.findUserEmail(principal.getName());
      user.ifPresent(appUser -> appUser.makeDefaultPhoneNumber(phoneNumber));
    }
    return phoneNumberService.update(phoneNumber, id);
  }


  @PutMapping("/{id}/merchant/{merchantId}")
  public PhoneNumber updateMerchantPhoneNumber(
      @RequestBody PhoneNumber phoneNumber,
      @PathVariable Long id, Principal principal,
      @PathVariable Long merchantId
  ) {
    if (phoneNumber.isDefault()) {
      var user = userService.findUserEmail(principal.getName());
      user.flatMap(appUser -> merchantService.getByIdAndUser(merchantId, appUser))
          .ifPresent(merchant -> merchant.makeDefaultPhoneNumber(phoneNumber));

      user.ifPresent(appUser -> appUser.makeDefaultPhoneNumber(phoneNumber));
    } else {
      throw new NotFoundException("Merchant not found for id: " + merchantId);
    }

    return phoneNumberService.update(phoneNumber, id);
  }
}
