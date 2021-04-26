package com.hubert.momoservice.config.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.hubert.momoservice.config.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class FileStore {
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Value("${aws.s3.endpointUrl}")
    private String endpointUrl;

    public FileStore(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(final MultipartFile multipartFile, String folderName) {
        String fileUrl = "";
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            final String fileName = generateFileName(file);
            fileUrl = endpointUrl + "/" + bucketName + "/" + folderName + "/" + fileName;
            uploadFileToS3Bucket(file, fileName, folderName);
            file.delete();
        } catch (final AmazonServiceException ex) {
            throw new BadRequestException("File failed to upload to Amazon S3. ");
        }

        return fileUrl;
    }

    public File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            throw new RuntimeException("Error in converting from multipartFile to File. ");
        }
        return file;
    }

    public void deleteFile(String fileUrl, String folderName) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/"));
        amazonS3.deleteObject(bucketName, folderName+fileName);
    }


    private void uploadFileToS3Bucket(final File file, String fileName, String folderName) {
        final PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, folderName+"/"+fileName, file);
        amazonS3.putObject(putObjectRequest);
    }

    public byte[] download(String fileUrl, String folderName) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        String path = String.format("%s/%s", bucketName, folderName);
        try {
            S3Object object = amazonS3.getObject(path, fileName);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download the file", e);
        }
    }
    private String generateFileName(File file){
        return LocalDateTime.now() + "_" +
                file.getName().replace(" ", "_");
    }
}
