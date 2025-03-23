package com.poliqlo.controllers.common.api.controller;

import com.poliqlo.controllers.common.api.model.request.SanPhamSearchRequest;
import com.poliqlo.controllers.common.api.service.SanPhamAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SanPhamRestController {

    private final SanPhamAPIService sanPhamAPIService;
    @GetMapping("/api/san-pham")
    public ResponseEntity<?> getAll(@ModelAttribute SanPhamSearchRequest request) {
        var resp=sanPhamAPIService.findAll(request);
        return ResponseEntity.ok(resp);
    }
    @GetMapping("/api/san-pham/{id}")
    public ResponseEntity<?> getById(@ModelAttribute SanPhamSearchRequest request, @PathVariable Long id) {
        request.setId(List.of(Math.toIntExact(id)));
        var lstResp=sanPhamAPIService.findAll(request).getContent();
        if(lstResp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy sản phẩm");
        }else{
            return ResponseEntity.ok(lstResp.get(0));
        }

    }
    @GetMapping("/api/san-pham-chi-tiet/{id}")
    public ResponseEntity<?> getAll(@PathVariable Integer id) {
        var resp=sanPhamAPIService.findSPCTById(id);
        return resp;
    }
    @GetMapping("/api/san-pham-chi-tiet")
    public ResponseEntity<?> getAllSPCT(@ModelAttribute SanPhamSearchRequest request) {
        var resp=sanPhamAPIService.findAllSPCT(request);
        return ResponseEntity.ok(resp);
    }



}
