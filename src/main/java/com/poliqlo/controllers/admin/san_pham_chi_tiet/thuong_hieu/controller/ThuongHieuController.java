package com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.controller;



import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.response.Response;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.service.ThuongHieuService;
import com.poliqlo.models.ThuongHieu;
import com.poliqlo.repositories.ThuongHieuRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ThuongHieuController {
    private static final Logger log = LoggerFactory.getLogger(ThuongHieuController.class);
    
    private final ModelMapper modelMapper;
    
    private final ThuongHieuRepository thuongHieuRepository;
    private final ThuongHieuService thuongHieuService;

    @GetMapping("/admin/san-pham-chi-tiet/thuong-hieu")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/thuong-hieu/thuong-hieu";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/thuong-hieu")
    public List<Response> getAll() {
        return modelMapper.map(thuongHieuRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, path = "/api/v1/san-pham-chi-tiet/thuong-hieu")
    public ResponseEntity<Response> add(@Valid @ModelAttribute AddRequest req) throws IOException {
        return thuongHieuService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/thuong-hieu")
    public ResponseEntity<Response> update(@Valid @ModelAttribute EditReq editReq) throws IOException {
        return thuongHieuService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/thuong-hieu")
    public ResponseEntity<ThuongHieu> delete(@RequestParam(name = "id") Integer id) {
        return thuongHieuService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/thuong-hieu/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return thuongHieuService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/thuong-hieu/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@RequestBody List<@Valid ImportReq> lstThuongHieu) {
        return thuongHieuService.importExcel(lstThuongHieu);
    }

    @GetMapping("/admin/san-pham-chi-tiet/thuong-hieu/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return thuongHieuService.exportToExcel();
    }
}
