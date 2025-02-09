package com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.controller;



import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.response.Response;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.service.KieuDangService;
import com.poliqlo.models.KieuDang;
import com.poliqlo.repositories.KieuDangRepository;
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
public class KieuDangController {
    private static final Logger log = LoggerFactory.getLogger(KieuDangController.class);
    
    private final ModelMapper modelMapper;
    
    private final KieuDangRepository kieuDangRepository;
    private final KieuDangService kieuDangService;

    @GetMapping("/admin/san-pham-chi-tiet/kieu-dang")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/kieu-dang/kieu-dang";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/kieu-dang")
    public List<Response> getAll() {
        return modelMapper.map(kieuDangRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/kieu-dang")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return kieuDangService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/kieu-dang")
    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
        return kieuDangService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/kieu-dang")
    public ResponseEntity<KieuDang> delete(@RequestParam(name = "id") Integer id) {
        return kieuDangService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/kieu-dang/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return kieuDangService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/kieu-dang/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstKieuDang) {
        return kieuDangService.importExcel(lstKieuDang);
    }

    @GetMapping("/admin/san-pham-chi-tiet/kieu-dang/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return kieuDangService.exportToExcel();
    }
}
