package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.LegalDocument;
import com.hubert.momoservice.service.LegalDocumentService;
import com.hubert.momoservice.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
public class LegalDocumentController {

    private final LegalDocumentService legalDocumentService;
    private final MerchantService merchantService;


    @Autowired
    public LegalDocumentController(
            LegalDocumentService legalDocumentService,
            MerchantService merchantService
    ) {
        this.legalDocumentService = legalDocumentService;
        this.merchantService = merchantService;
    }

    @GetMapping("/{merchantId}")
    public List<LegalDocument> getDevices(@PathVariable Long merchantId){
        var merchant =  merchantService.getOne(merchantId);

        if(merchant.isPresent()){
            return legalDocumentService.getAllByMerchant(merchant.get());
        }else {
            throw new NotFoundException("No merchant found for id: " + merchantId);
        }
    }


    @GetMapping("/{id}")
    public LegalDocument getOne(@PathVariable Long id){
        return legalDocumentService
                .getOne(id)
                .orElseThrow( () -> new NotFoundException("legal not found for id: " + id));
    }

    @PostMapping("/{merchantId}")
    public LegalDocument addNewDevice(
            @Valid @RequestBody LegalDocument legalDocument,
            @PathVariable long merchantId
    ){
        var merchant = merchantService.getOne(merchantId);

        if(merchant.isPresent()){
            legalDocument.setMerchant(merchant.get());
        }else
            throw new NotFoundException("Merchant not found for id: " + merchant);

        try{
            return legalDocumentService.save(legalDocument);
        }catch (DataIntegrityViolationException e){
            throw new BadRequestException(
                    e.getCause().getMessage() +
                            ", (documentUrl) = (" + legalDocument.getDocumentUrl() + ") already exists");
        }
    }

    @PutMapping("/{id}")
    public LegalDocument updateFingerprint
            (@Valid @RequestBody LegalDocument legalDocument, @PathVariable Long id){
      return legalDocumentService.update(legalDocument, id);
    }
}
