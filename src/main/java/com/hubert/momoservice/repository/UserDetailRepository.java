package com.hubert.momoservice.repository;

import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

  Optional<UserDetail> findUserDetailByAppUser(AppUser appUser);

  List<UserDetail> findAllByTownAndRegion(String town, String region);

  List<UserDetail> findAllByCityAndRegionAndTownIsNot(String city, String region, String town);

  List<UserDetail> findAllByRegionAndTownIsNotAndCityIsNot(String region, String town, String city);

  List<UserDetail> findAllByRegionIsNot(String region);
}
