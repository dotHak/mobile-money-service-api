package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.UserDetail;
import com.hubert.momoservice.service.AppUserService;
import com.hubert.momoservice.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/usersDetails")
public class UserDetailController {

    private final UserDetailService userDetailService;
    private final AppUserService userService;

    @Autowired
    public UserDetailController(UserDetailService userDetailService, AppUserService userService) {
        this.userDetailService = userDetailService;
        this.userService = userService;
    }

    @GetMapping
    public UserDetail getCurrentUserDetail(Principal principal){
        var user = userService.findUserEmail(principal.getName());

        if(user.isPresent()){
            return userDetailService
                    .getUserDetailByUser(user.get())
                    .orElseThrow(() -> new NotFoundException("User details not found for current user"));
        }else {
            throw new NotFoundException("No user found");
        }
    }


    @GetMapping("/{id}")
    public UserDetail getOne(@PathVariable Long id){
        return userDetailService
                .getOne(id)
                .orElseThrow( () -> new NotFoundException("User details not found for id: " + id));
    }

    @PostMapping
    public UserDetail addUserDetail
            (@Valid @RequestBody UserDetail userDetail, Principal principal){
        var user = userService.findUserEmail(principal.getName());

        user.ifPresent(userDetail::setUser);

        return userDetailService.save(userDetail);
    }

    @PutMapping("/{id}")
    public UserDetail updateUserDetail
            (@RequestBody UserDetail userDetail, @PathVariable Long id){
      return userDetailService.update(userDetail, id);
    }
}
