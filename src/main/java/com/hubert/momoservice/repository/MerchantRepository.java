package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    public List<Merchant> findAllByAppUser(AppUser appUser);

    public Optional<Merchant> findMerchantByAppUserAndId(AppUser appUser, Long id);

}
