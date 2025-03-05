package com.poliqlo.controllers.admin.chi_tiet_san_pham.controller;



import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request.AddRequest;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request.EditReq;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request.ImportReq;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.response.Response;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.service.SanPhamChiTietService;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChiTietSanPhamController {
    private static final Logger log = LoggerFactory.getLogger(ChiTietSanPhamController.class);
    
    private final ModelMapper modelMapper;
    
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamChiTietService sanPhamChiTietService;

    @GetMapping("/admin/san-pham-chi-tiet/san-pham-chi-tiet")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/san-pham-chi-tiet/san-pham-chi-tiet";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/san-pham-chi-tiet")
    public List<Response> getAll() {
        return modelMapper.map(sanPhamChiTietRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/san-pham-chi-tiet")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return sanPhamChiTietService.save(req);
    }


//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/san-pham-chi-tiet")
//    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
//        return sanPhamChiTietService.update(editReq);
//    }
    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/san-pham-chi-tiet")
    public ResponseEntity<SanPhamChiTiet> delete(@RequestParam(name = "id") Integer id) {
        return sanPhamChiTietService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/san-pham-chi-tiet/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return sanPhamChiTietService.revert(id);
    }
    //edit
    @ResponseBody
    @PostMapping("/api/v1/san-pham-detail/update")
    public ResponseEntity<?> updateProductDetail(@Valid @RequestBody SanPhamChiTiet spctUpdate) {
        SanPhamChiTiet updated = sanPhamChiTietService.updateSanPhamChiTiet(spctUpdate);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Sản phẩm chi tiết không tồn tại!");
        }
        return ResponseEntity.ok(updated);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/san-pham-chi-tiet/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstSanPhamChiTiet) {
        return sanPhamChiTietService.importExcel(lstSanPhamChiTiet);
    }

    @GetMapping("/admin/san-pham-chi-tiet/san-pham-chi-tiet/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return sanPhamChiTietService.exportToExcel();
    }
}
