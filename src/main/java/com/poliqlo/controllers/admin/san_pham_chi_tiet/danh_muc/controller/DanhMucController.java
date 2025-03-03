package com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.controller;



import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.response.Response;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.service.DanhMucService;
import com.poliqlo.models.DanhMuc;
import com.poliqlo.repositories.DanhMucRepository;
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
public class DanhMucController {
    private static final Logger log = LoggerFactory.getLogger(DanhMucController.class);
    
    private final ModelMapper modelMapper;
    
    private final DanhMucRepository danhMucRepository;
    private final DanhMucService danhMucService;

    @GetMapping("/admin/san-pham-chi-tiet/danh-muc")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/danh-muc/danh-muc";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/danh-muc")
    public List<Response> getAll() {
        return modelMapper.map(danhMucRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/danh-muc")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return danhMucService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/danh-muc")
    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
        return danhMucService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/danh-muc")
    public ResponseEntity<DanhMuc> delete(@RequestParam(name = "id") Integer id) {
        return danhMucService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/danh-muc/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return danhMucService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/danh-muc/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstDanhMuc) {
        return danhMucService.importExcel(lstDanhMuc);
    }

    @GetMapping("/admin/san-pham-chi-tiet/danh-muc/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return danhMucService.exportToExcel();
    }
}
