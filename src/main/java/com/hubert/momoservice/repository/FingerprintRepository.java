package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.Fingerprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Long> {
    Optional<Fingerprint> findFingerprintByAppUser(AppUser appUser);
}
