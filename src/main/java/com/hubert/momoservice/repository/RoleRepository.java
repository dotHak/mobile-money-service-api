package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.Role;
import com.hubert.momoservice.entity.RoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Short> {

    Optional<Role> findRoleByName(RoleType roleName);
}
