package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.User;
import com.hubert.momoservice.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    public Optional<UserDetail> findUserDetailByUser_Id(long id);

    public Optional<UserDetail> findUserDetailByUser(User user);

}
