package com.hubert.momoservice.controller;

import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {

    private final RegistrationService registrationService;

    @Autowired
    public UserController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>>
    register(@Valid @RequestBody AppUser appUser){
        HashMap<String, String> map = new HashMap<>();
        map.put("token", registrationService.register(appUser));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping(path = "/signup/confirm")
    public ResponseEntity<Map<String, String>>
    confirm(@RequestParam("token") String token) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", registrationService.confirmToken(token));

        return new ResponseEntity<>(map, HttpStatus.OK);
    }



}
