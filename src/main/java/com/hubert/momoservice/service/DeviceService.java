package com.hubert.momoservice.service;

import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.Device;
import com.hubert.momoservice.entity.Merchant;
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

  public List<Device> getAllByMerchant(Merchant merchant) {
    return repository.findAllByMerchant(merchant);
  }

  public Device update(Device device, Long id) {

    return repository.findById(id).map(oldDevice -> {
      oldDevice.setName(device.getName());
      return repository.save(oldDevice);
    }).orElseThrow(() -> new NotFoundException("No device found for id: " + id));
  }
}
