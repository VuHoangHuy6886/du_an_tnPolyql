package com.poliqlo.controllers.common.file.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.poliqlo.controllers.common.file.model.response.BlobResponse;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BlobStoreService {
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;
    @Value("${spring.cloud.azure.storage.blob.temp-container-name}")
    private String tempContainerName;
    @Value("${spring.cloud.azure.storage.connection-string}")
    private String connectionString;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void init() {
        System.out.println(connectionString);
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
    public List<BlobResponse> uploadMultiTempImage(List<MultipartFile> images) throws IOException {
        List<BlobResponse> lstImageResponse = new ArrayList<>();
        for (MultipartFile image : images) {
            lstImageResponse.add(uploadTempFile(image));
        }
        return lstImageResponse;
    }
    public BlobResponse upload(MultipartFile file) throws IOException {
        return upload(file,containerName);
    }
    public BlobResponse uploadTempFile(MultipartFile image) throws IOException {
        return upload(image,tempContainerName);
    }
    public BlobResponse upload(MultipartFile file, String containerName) throws IOException {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        var url=blobClient.getBlobUrl();
        return new BlobResponse (fileName, url);
    }
    public void delete(String url) {
        try {
            String regex = ".*/([^/]+)/([^/]+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);

            if (matcher.find()) {
                String containerName = matcher.group(1);
                String fileName = matcher.group(2);
                BlobClient blobClient = blobServiceClient
                        .getBlobContainerClient(containerName)
                        .getBlobClient(fileName);
                blobClient.delete();
            } else {
                System.out.println("File không tồn tại");
            }
        }catch (Exception e){
            System.out.println("Lỗi khi xóa file");
        }
    }
    public String moveImageToPermanent(String fileName) {
        BlobClient tempBlobClient = blobServiceClient
                .getBlobContainerClient(tempContainerName)
                .getBlobClient(fileName);

        BlobClient permBlobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(fileName);

        permBlobClient.beginCopy(tempBlobClient.getBlobUrl(), null);
//        tempBlobClient.delete();
        return permBlobClient.getBlobUrl();
    }

}
