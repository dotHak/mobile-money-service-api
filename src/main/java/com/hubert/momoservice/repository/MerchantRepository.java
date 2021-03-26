package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Merchant;
import com.hubert.momoservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    public List<Merchant> findAllByUser_Id(Long id);

    public List<Merchant> findAllByUser(User user);

}
