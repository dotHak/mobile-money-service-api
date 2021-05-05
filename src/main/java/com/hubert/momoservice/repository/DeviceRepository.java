package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Device;
import com.hubert.momoservice.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

  public List<Device> findAllByMerchant(Merchant merchant);
}
