package com.poliqlo.controllers.common.api.controller;

import com.poliqlo.controllers.common.api.model.request.SanPhamSearchRequest;
import com.poliqlo.controllers.common.api.service.SanPhamAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SanPhamRestController {

    private final SanPhamAPIService sanPhamAPIService;
    @GetMapping("/api/san-pham")
    public ResponseEntity<?> getAll(@ModelAttribute SanPhamSearchRequest request) {
        var resp=sanPhamAPIService.findAll(request);
        return resp;
    }


}
