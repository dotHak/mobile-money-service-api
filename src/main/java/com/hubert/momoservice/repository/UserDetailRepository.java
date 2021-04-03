package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    public Optional<UserDetail> findUserDetailByAppUser(AppUser appUser);

}
