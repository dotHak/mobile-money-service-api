package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Network;
import com.hubert.momoservice.entity.NetworkType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkRepository extends CrudRepository<Network, Short> {

  Optional<Network> findNetworkByName(NetworkType networkName);
}
