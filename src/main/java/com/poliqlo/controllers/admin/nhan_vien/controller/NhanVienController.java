package com.poliqlo.controllers.admin.nhan_vien.controller;



import com.poliqlo.controllers.admin.nhan_vien.model.request.AddRequest;
import com.poliqlo.controllers.admin.nhan_vien.model.request.EditReq;
import com.poliqlo.controllers.admin.nhan_vien.model.request.ImportReq;
import com.poliqlo.controllers.admin.nhan_vien.model.response.Response;
import com.poliqlo.controllers.admin.nhan_vien.service.NhanVienService;
import com.poliqlo.models.NhanVien;
import com.poliqlo.repositories.NhanVienRepository;
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
public class NhanVienController {
    private static final Logger log = LoggerFactory.getLogger(NhanVienController.class);
    
    private final ModelMapper modelMapper;
    
    private final NhanVienRepository nhanVienRepository;
    private final NhanVienService nhanVienService;

    @GetMapping("/admin/nhan-vien")
    public String ui(Model model) {
        return "/admin/nhan-vien/nhan-vien";
    }

    @ResponseBody
    @GetMapping("/api/v1/nhan-vien")
    public List<Response> getAll() {
        return modelMapper.map(nhanVienService.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/nhan-vien")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return nhanVienService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/nhan-vien")
    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
        return nhanVienService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/nhan-vien")
    public ResponseEntity<NhanVien> delete(@RequestParam(name = "id") Integer id) {
        return nhanVienService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/nhan-vien/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return nhanVienService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/nhan-vien/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstNhanVien) {
        return nhanVienService.importExcel(lstNhanVien);
    }

    @GetMapping("/admin/nhan-vien/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return nhanVienService.exportToExcel();
    }
}
