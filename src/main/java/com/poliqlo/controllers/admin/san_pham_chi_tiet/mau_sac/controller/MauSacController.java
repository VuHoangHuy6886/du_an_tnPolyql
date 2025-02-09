package com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.controller;



import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.response.Response;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.service.MauSacService;
import com.poliqlo.models.MauSac;
import com.poliqlo.repositories.MauSacRepository;
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
public class MauSacController {

    private final ModelMapper modelMapper;
    
    private final MauSacRepository mauSacRepository;
    private final MauSacService mauSacService;

    @GetMapping("/admin/san-pham-chi-tiet/mau-sac")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/mau-sac/mau-sac";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public List<Response> getAll() {
        return modelMapper.map(mauSacRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return mauSacService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
        return mauSacService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public ResponseEntity<MauSac> delete(@RequestParam(name = "id") Integer id) {
        return mauSacService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return mauSacService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstMauSac) {
        return mauSacService.importExcel(lstMauSac);
    }

    @GetMapping("/admin/san-pham-chi-tiet/mau-sac/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return mauSacService.exportToExcel();
    }
}
