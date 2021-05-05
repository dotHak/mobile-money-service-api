package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Status;
import com.hubert.momoservice.entity.StatusType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends CrudRepository<Status, Short> {

  Optional<Status> findStatusByName(StatusType statusName);
}
