package com.poliqlo.controllers.common.file.controller;

import com.poliqlo.controllers.common.file.model.response.BlobResponse;
import com.poliqlo.controllers.common.file.service.BlobStoreService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final BlobStoreService blobStoreImageService;
    @PostMapping("image/upload-temp")
    public List<BlobResponse> upload_temp_image(@RequestParam("image") List<MultipartFile> imageName){
        try {
            return blobStoreImageService.uploadMultiTempImage(imageName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Lỗi upload");
        }
    }

}
