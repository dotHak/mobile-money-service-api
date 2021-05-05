package com.hubert.momoservice.service;

import com.hubert.momoservice.config.aws.FileStore;
import com.hubert.momoservice.config.exception.NotFoundException;
import com.hubert.momoservice.entity.Fingerprint;
import com.hubert.momoservice.repository.FingerprintRepository;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class FingerprintService implements GenericService<Fingerprint, Long> {

  private static final String FOLDER_NAME = "fingerprints";

  private final FingerprintRepository repository;

  private final FileStore fileStore;

  @Autowired
  public FingerprintService(FingerprintRepository repository, FileStore fileStore) {
    this.repository = repository;
    this.fileStore = fileStore;
  }

  @Override
  public List<Fingerprint> getAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Fingerprint> getOne(Long id) {
    return repository.findById(id);
  }

  @Override
  public Fingerprint save(Fingerprint fingerprint) {
    return repository.save(fingerprint);
  }


  public Fingerprint update(Fingerprint fingerprint, Long id) {

    return repository.findById(id).map(oldFingerprint -> {
      oldFingerprint.setImageUrl(fingerprint.getImageUrl());
      return repository.save(oldFingerprint);
    }).orElseThrow(() -> new NotFoundException("No fingerprint found for id: " + id));
  }

  public Fingerprint updateUrl(String url, Long id, MultipartFile multipartFile) {
    return repository.findById(id).map(oldFingerprint -> {
      deleteFile(oldFingerprint.getImageUrl());
      oldFingerprint.setImageUrl(url);
      oldFingerprint.setByteData(getByteData(multipartFile));
      return repository.save(oldFingerprint);
    }).orElseThrow(() -> new NotFoundException("No fingerprint found for id: " + id));
  }


  public String uploadFile(MultipartFile multipartFile) {
    return fileStore.uploadFile(multipartFile, FOLDER_NAME);
  }

  public byte[] getFile(Fingerprint fingerprint) {
    return fileStore.download(fingerprint.getImageUrl(), FOLDER_NAME);
  }

  @Async
  public void deleteFile(String fileUrl) {
    fileStore.deleteFile(fileUrl, FOLDER_NAME);
  }

  public File convertToFile(MultipartFile multipartFile) {
    return fileStore.convertMultiPartFileToFile(multipartFile);
  }

  public byte[] getByteData(MultipartFile multipartFile) {
    try {
      FingerprintTemplate fingerprintTemplate = new FingerprintTemplate(
          new FingerprintImage(
              Files.readAllBytes(convertToFile(multipartFile).toPath()),
              new FingerprintImageOptions()
                  .dpi(500)));
      return fingerprintTemplate.toByteArray();
    } catch (IOException exception) {
      throw new RuntimeException("Error reading file!");
    }
  }
}
