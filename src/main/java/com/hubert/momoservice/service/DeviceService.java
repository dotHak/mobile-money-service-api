package com.hubert.momoservice.service;

import com.hubert.momoservice.entity.Device;
import com.hubert.momoservice.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService implements GenericService<Device, Long> {

    private final DeviceRepository repository;

    @Autowired
    public DeviceService(DeviceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Device> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Device> getOne(Long id) {
        return repository.findById(id);
    }

    @Override
    public Device save(Device device) {
        return repository.save(device);
    }
}
