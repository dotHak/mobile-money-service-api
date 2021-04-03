package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.Device;
import com.hubert.momoservice.service.DeviceService;
import com.hubert.momoservice.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;
    private final MerchantService merchantService;


    @Autowired
    public DeviceController(
            DeviceService deviceService,
            MerchantService merchantService
    ) {
        this.deviceService = deviceService;
        this.merchantService = merchantService;
    }

    @GetMapping("/{merchantId}")
    public List<Device> getDevices(@PathVariable Long merchantId){
        var merchant =  merchantService.getOne(merchantId);

        if(merchant.isPresent()){

            return deviceService.getAllByMerchant(merchant.get());
        }else {
            throw new NotFoundException("No merchant found for id: " + merchantId);
        }
    }


    @GetMapping("/{id}")
    public Device getOne(@PathVariable Long id){
        return deviceService
                .getOne(id)
                .orElseThrow( () -> new NotFoundException("Device not found for id: " + id));
    }

    @PostMapping("/{merchantId}")
    public Device addNewDevice(
            @Valid @RequestBody Device device,
            @PathVariable long merchantId
    ){
        var merchant = merchantService.getOne(merchantId);

        if(merchant.isPresent()){
            device.setMerchant(merchant.get());
        }else
            throw new NotFoundException("Merchant not found for id: " + merchant);

        try{
            return deviceService.save(device);
        }catch (DataIntegrityViolationException e){
            throw new BadRequestException(
                    e.getCause().getMessage() + ", (name) = (" + device.getName() + ") already exists");
        }
    }

    @PutMapping("/{id}")
    public Device updateFingerprint
            (@Valid @RequestBody Device device, @PathVariable Long id){
      return deviceService.update(device, id);
    }
}
