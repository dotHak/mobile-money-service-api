package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.Fingerprint;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.FingerprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/fingerprints")
public class FingerprintController {

    private final FingerprintService fingerprintService;
    private final AppUserService userService;

    @Autowired
    public FingerprintController(FingerprintService fingerprintService, AppUserService userService) {
        this.fingerprintService = fingerprintService;
        this.userService = userService;
    }

    @GetMapping
    public Fingerprint getFingerprints(Principal principal){
        var user = userService.findUserEmail(principal.getName());

        if(user.isPresent()){
            var finger =  fingerprintService.getFingerprintByUser(user.get());

            if (finger.isPresent())
                return finger.get();
            else
                throw new NotFoundException("No fingerprint found");
        }else {
            throw new NotFoundException("No user found");
        }
    }


    @GetMapping("/{id}")
    public Fingerprint getOne(@PathVariable Long id){
        return fingerprintService
                .getOne(id)
                .orElseThrow( () -> new NotFoundException("Fingerprint not found for id: " + id));
    }

    @PostMapping
    public Fingerprint addNewFingerprint
            (@Valid @RequestBody Fingerprint fingerprint, Principal principal){
        var user = userService.findUserEmail(principal.getName());

        user.ifPresent(fingerprint::setUser);

        try{
            return fingerprintService.save(fingerprint);
        }catch (DataIntegrityViolationException e){
            throw new BadRequestException(
                    e.getCause().getMessage() + ", (url) = (" + fingerprint.getImageUrl() + ") already exists");
        }
    }

    @PutMapping("/{id}")
    public Fingerprint updateFingerprint
            (@Valid @RequestBody Fingerprint fingerprint, @PathVariable Long id){
      return fingerprintService.update(fingerprint, id);
    }
}
