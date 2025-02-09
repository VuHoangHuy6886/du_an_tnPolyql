package com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.controller;



import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.response.Response;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.service.KichThuocService;
import com.poliqlo.models.KichThuoc;
import com.poliqlo.repositories.KichThuocRepository;
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
public class KichThuocController {
    private static final Logger log = LoggerFactory.getLogger(KichThuocController.class);
    
    private final ModelMapper modelMapper;
    
    private final KichThuocRepository kichThuocRepository;
    private final KichThuocService kichThuocService;

    @GetMapping("/admin/san-pham-chi-tiet/kich-thuoc")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/kich-thuoc/kich-thuoc";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/kich-thuoc")
    public List<Response> getAll() {
        return modelMapper.map(kichThuocRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/kich-thuoc")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return kichThuocService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/kich-thuoc")
    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
        return kichThuocService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/kich-thuoc")
    public ResponseEntity<KichThuoc> delete(@RequestParam(name = "id") Integer id) {
        return kichThuocService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/kich-thuoc/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return kichThuocService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/kich-thuoc/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstKichThuoc) {
        return kichThuocService.importExcel(lstKichThuoc);
    }

    @GetMapping("/admin/san-pham-chi-tiet/kich-thuoc/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return kichThuocService.exportToExcel();
    }
}
