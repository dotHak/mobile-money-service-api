package com.hubert.momoservice.controller;

import com.hubert.momoservice.config.exception.BadRequestException;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.LegalDocument;
import com.hubert.momoservice.service.LegalDocumentService;
import com.hubert.momoservice.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
  public List<LegalDocument> getDocuments(@PathVariable Long merchantId) {
    var merchant = merchantService.getOne(merchantId);

    if (merchant.isPresent()) {
      return legalDocumentService.getAllByMerchant(merchant.get());
    } else {
      throw new NotFoundException("No merchant found for id: " + merchantId);
    }
  }


  @GetMapping("/{id}")
  public LegalDocument getOne(@PathVariable Long id) {
    return legalDocumentService
        .getOne(id)
        .orElseThrow(() -> new NotFoundException("legal not found for id: " + id));
  }

  @PostMapping("/{merchantId}")
  public LegalDocument addNewDevice(
      @RequestPart(value = "file") final MultipartFile multipartFile,
      @PathVariable long merchantId
  ) {
    var merchant = merchantService.getOne(merchantId);

    if (merchant.isPresent()) {
      String url = legalDocumentService.uploadFile(multipartFile);
      LegalDocument legalDocument = new LegalDocument(url, merchant.get());
      try {
        return legalDocumentService.save(legalDocument);
      } catch (DataIntegrityViolationException e) {
        throw new BadRequestException(
            e.getCause().getMessage() +
                ", (documentUrl) = (" + legalDocument.getDocumentUrl() + ") already exists");
      }
    } else {
      throw new NotFoundException("Merchant not found for id: " + merchant);
    }


  }

  @PutMapping("/{id}")
  public LegalDocument updateFingerprint(
      @RequestPart(value = "file") final MultipartFile multipartFile,
      @PathVariable Long id) {
    String url = legalDocumentService.uploadFile(multipartFile);
    return legalDocumentService.updateUrl(url, id);
  }

  @GetMapping(value = "{id}/download")
  public ResponseEntity<ByteArrayResource> downloadTodoImage(@PathVariable("id") Long id) {
    Optional<LegalDocument> legalDocument = legalDocumentService.getOne(id);
    if (legalDocument.isEmpty()) {
      throw new NotFoundException("No document found for id: " + id);
    }

    byte[] file = legalDocumentService.getFile(legalDocument.get());
    String fileUrl = legalDocument.get().getDocumentUrl();
    final ByteArrayResource resource = new ByteArrayResource(file);
    return ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_PDF)
        .header("Content-disposition",
            "attachment; filename=\"" + fileUrl.substring(fileUrl.lastIndexOf("/") + 1) + "\"")
        .body(resource);
  }
}
