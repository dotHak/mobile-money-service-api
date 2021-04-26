package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.*;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.FingerprintService;
import com.hubert.momoservice.service.TransactionService;
import com.hubert.momoservice.service.UserDetailService;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fingerprints")
public class FingerprintController {

    private final FingerprintService fingerprintService;
    private final UserDetailService userDetailService;
    private final AppUserService userService;
    private final TransactionService transactionService;

    @Autowired
    public FingerprintController(
            FingerprintService fingerprintService,
            UserDetailService userDetailService,
            AppUserService userService,
            TransactionService transactionService) {
        this.fingerprintService = fingerprintService;
        this.userDetailService = userDetailService;
        this.userService = userService;
        this.transactionService = transactionService;
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

    @PostMapping
    public Fingerprint addNewFingerprint(
            @RequestPart(value= "file") final MultipartFile multipartFile,
            Principal principal) {
        var user = userService.findUserEmail(principal.getName());

        if(user.isPresent()){
            var finger =  fingerprintService.getFingerprintByUser(user.get());
            finger.ifPresent(fingerprint -> {
                throw new BadRequestException("Fingerprint already exists");
            });

            String url = fingerprintService.uploadFile(multipartFile);
            Fingerprint fingerprint = new Fingerprint(url, user.get());

            try{
                return fingerprintService.save(fingerprint);
            }catch (DataIntegrityViolationException e){
                throw new BadRequestException(
                        e.getCause().getMessage() + ", (url) = (" + fingerprint.getImageUrl() + ") already exists");
            }
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

    @PutMapping("/{id}")
    public Fingerprint updateFingerprint(
            @RequestPart(value= "file") final MultipartFile multipartFile,
             @PathVariable Long id){
        String url = fingerprintService.uploadFile(multipartFile);
        return fingerprintService.updateUrl(url, id);
    }

    @PostMapping("/authenticate")
    public Boolean authenticateFingerprint(
            @RequestPart(value= "file") final MultipartFile multipartFile,
            Principal principal,
            @RequestParam(value = "price") double price
    ){
        var user = userService.findUserEmail(principal.getName());
        if(user.isPresent()){
            var userDetail = userDetailService.getUserDetailByUser(user.get());
            if(userDetail.isEmpty()){
                throw new NotFoundException("User detail not found!");
            }

            var userDetails = userDetail.get();

            var sender = searchAll(userDetails, multipartFile);
            if(sender != null){
                Transaction transaction = new Transaction(
                        sender.getDefaultPhoneNumber(),
                        userDetails.getUser().getDefaultPhoneNumber(),
                        price,
                        new Status((short) 4,StatusType.PENDING)
                );

                transactionService.save(transaction);
                
                return true;
            }

            return false;

        }
        else {
            throw new NotFoundException("User not found!");
        }
    }

    private AppUser contains(List<UserDetail> userDetailList, FingerprintTemplate probe){

        double threshold = 40;
        boolean matches = false;


        for(var userDetail: userDetailList){
            if(userDetail.getUser().getFingerprint() != null){
                var fileBytes = fingerprintService.getFile(userDetail.getUser().getFingerprint());
                FingerprintTemplate candidate = new FingerprintTemplate(
                        new FingerprintImage(
                                fileBytes,
                                new FingerprintImageOptions()
                                        .dpi(500)));

                double score = new FingerprintMatcher(probe)
                        .match(candidate);
                matches = score >= threshold;

                if (matches){
                    return userDetail.getUser();
                }
            }
        }


        return null;
    }


    private AppUser searchAll(UserDetail userDetail, MultipartFile multipartFile){

        boolean isFound = false;
        AppUser appUser;
        var theList = userDetailService.findAllByTown(userDetail);
        var file = fingerprintService.convertToFile(multipartFile);
        try{
            FingerprintTemplate probe = new FingerprintTemplate(
                    new FingerprintImage(
                            Files.readAllBytes(file.toPath()),
                            new FingerprintImageOptions()
                                    .dpi(500)));

            appUser = contains(theList, probe);
            isFound = appUser != null;


            if(!isFound){
                theList = userDetailService.findAllByCity(userDetail);
                appUser = contains(theList, probe);
                isFound = appUser != null;
                if (isFound)
                    return appUser;
            }

            if(!isFound){
                theList = userDetailService.findAllByRegion(userDetail);
                appUser = contains(theList, probe);
                isFound = appUser != null;

                if (isFound)
                    return appUser;
            }

            if(!isFound){
                theList = userDetailService.findAllExceptRegion(userDetail);
                appUser = contains(theList, probe);
                isFound = appUser != null;

                if (isFound)
                    return appUser;
            }

        }catch (IOException exception){
            throw new RuntimeException("Error reading file!");
        }


        return appUser;
    }



}
