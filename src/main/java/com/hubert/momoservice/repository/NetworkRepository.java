package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Network;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkRepository extends CrudRepository<Network, Integer> {
}
