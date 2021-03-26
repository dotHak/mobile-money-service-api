package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Fingerprint;
import com.hubert.momoservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FingerprintRepository extends JpaRepository<Fingerprint, Long> {

    public Optional<Fingerprint> findFingerprintByUser_Id(Long id);

    public Optional<Fingerprint> findFingerprintByUser(User user);
}
