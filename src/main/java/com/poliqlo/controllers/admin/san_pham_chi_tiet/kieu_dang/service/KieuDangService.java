package com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.service;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.response.Response;
import com.poliqlo.models.KieuDang;
import com.poliqlo.repositories.KieuDangRepository;
import com.poliqlo.utils.ExportExcel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KieuDangService {
    private final ModelMapper modelMapper;
    private final KieuDangRepository kieuDangRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        KieuDang kieuDang = modelMapper.map(req, KieuDang.class);
        Response kieuDangResponse = modelMapper.map(kieuDangRepository.save(kieuDang), Response.class);
        return new ResponseEntity<>(kieuDangResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        kieuDangRepository.save(modelMapper.map(editReq, KieuDang.class));
        var resp= kieuDangRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<KieuDang> delete(Integer id) {
        if (id == null || !kieuDangRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var kieuDangEditRequest = kieuDangRepository.findById(id).get();
        kieuDangEditRequest.setIsDeleted(true);
        var kieuDang = modelMapper.map(kieuDangEditRequest, Response.class);
        var resp=kieuDangRepository.save(kieuDangEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !kieuDangRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var kieuDangEditRequest = kieuDangRepository.findById(id).get();
        kieuDangEditRequest.setIsDeleted(false);
        var kieuDang = modelMapper.map(kieuDangEditRequest, Response.class);
        kieuDangRepository.save(kieuDangEditRequest);
        return ResponseEntity.accepted().body(kieuDang);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstKieuDang) {

        try{
            List<KieuDang> lstMS = lstKieuDang.stream()
                    .map((element) -> modelMapper.map(element, KieuDang.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = kieuDangRepository.saveAll(lstMS).stream()
                    .map((element) -> modelMapper.map(element, Response.class))
                    .collect(Collectors.toList());
            return ResponseEntity.accepted().body(lstRP);
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
    }

    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<KieuDang> mauSacList = kieuDangRepository.findAll();
        ExportExcel<KieuDang> exportExcel = new ExportExcel<KieuDang> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=kieu_dang.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
