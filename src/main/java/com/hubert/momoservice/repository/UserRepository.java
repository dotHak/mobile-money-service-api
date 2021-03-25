package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
