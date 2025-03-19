package com.poliqlo.controllers.admin.gio_hang.controller;


import com.poliqlo.controllers.admin.gio_hang.model.response.Response;
import com.poliqlo.controllers.admin.gio_hang.service.GioHangService;

import com.poliqlo.controllers.admin.san_pham.model.request.AddRequestNBC;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.GioHangChiTiet;
import com.poliqlo.models.NhanVien;
import com.poliqlo.models.TaiKhoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/gio-hang")
public class GioHangController {
    @Autowired
    private GioHangService gioHangService;

    @Autowired
    private BlobStoreService blobStoreService;

    @PostMapping
    public ResponseEntity<GioHangChiTiet> addToCart(@RequestBody Response request) {
        return ResponseEntity.ok(gioHangService.addToCart(request));
    }



}


