package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.AppUser;
import com.hubert.momoservice.entity.UserDetail;
import com.hubert.momoservice.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements GenericService<UserDetail, Long>{

    private final UserDetailRepository repository;

    @Autowired
    public UserDetailService(UserDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDetail> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserDetail> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public UserDetail save(UserDetail userDetail) {
        return repository.save(userDetail);
    }

    public Optional<UserDetail> getUserDetailByUser(AppUser appUser){
        return repository.findUserDetailByAppUser(appUser);
    }

    public UserDetail update(UserDetail userDetail, Long id){

        return repository.findById(id)
                .map( oldDetail -> {
                    String firstName = userDetail.getFirstName() == null ?
                            oldDetail.getFirstName() : userDetail.getFirstName();
                    String lastName = userDetail.getLastName() == null ?
                            oldDetail.getLastName() : userDetail.getLastName();
                    String middleName = userDetail.getMiddleName() == null ?
                            oldDetail.getMiddleName() : userDetail.getMiddleName();
                    String houseNumber = userDetail.getHouseNumber() == null ?
                            oldDetail.getHouseNumber() : userDetail.getHouseNumber();
                    String city = userDetail.getCity() == null ?
                            oldDetail.getCity() : userDetail.getCity();
                    String region = userDetail.getRegion() == null ?
                            oldDetail.getRegion() : userDetail.getRegion();
                    String town = userDetail.getTown() == null ?
                            oldDetail.getTown() : userDetail.getTown();

                    oldDetail.setFirstName(firstName);
                    oldDetail.setLastName(lastName);
                    oldDetail.setMiddleName(middleName);
                    oldDetail.setHouseNumber(houseNumber);
                    oldDetail.setCity(city);
                    oldDetail.setRegion(region);
                    oldDetail.setTown(town);

                    return repository.save(oldDetail);
                })
                .orElseThrow(() -> new NotFoundException("No user details found for id: " + id));

    }

    public List<UserDetail> findAllByTown(UserDetail userDetail){
        return repository.findAllByTownAndRegion(userDetail.getTown(), userDetail.getRegion());
    }

    public List<UserDetail> findAllByCity(UserDetail userDetail){
        return repository.findAllByCityAndRegionAndTownIsNot(
                userDetail.getCity(),
                userDetail.getRegion(),
                userDetail.getTown()
        );
    }

    public List<UserDetail> findAllByRegion(UserDetail userDetail){
        return repository.findAllByRegionAndTownIsNotAndCityIsNot(
                userDetail.getRegion(),
                userDetail.getTown(),
                userDetail.getCity()
        );
    }

    public List<UserDetail> findAllExceptRegion(UserDetail userDetail){
        return repository.findAllByRegionIsNot(userDetail.getRegion());
    }


}
