package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

  Boolean existsPhoneNumberByNumber(String phoneNumber);

  Optional<PhoneNumber> findPhoneNumberByNumber(String phoneNumber);
}
