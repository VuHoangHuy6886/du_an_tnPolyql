package com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.service;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.response.Response;
import com.poliqlo.models.KichThuoc;
import com.poliqlo.repositories.KichThuocRepository;
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
public class KichThuocService {
    private final ModelMapper modelMapper;
    private final KichThuocRepository kichThuocRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        KichThuoc kichThuoc = modelMapper.map(req, KichThuoc.class);
        Response kichThuocResponse = modelMapper.map(kichThuocRepository.save(kichThuoc), Response.class);
        return new ResponseEntity<>(kichThuocResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        kichThuocRepository.save(modelMapper.map(editReq, KichThuoc.class));
        var resp= kichThuocRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<KichThuoc> delete(Integer id) {
        if (id == null || !kichThuocRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var kichThuocEditRequest = kichThuocRepository.findById(id).get();
        kichThuocEditRequest.setIsDeleted(true);
        var kichThuoc = modelMapper.map(kichThuocEditRequest, Response.class);
        var resp=kichThuocRepository.save(kichThuocEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !kichThuocRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var kichThuocEditRequest = kichThuocRepository.findById(id).get();
        kichThuocEditRequest.setIsDeleted(false);
        var kichThuoc = modelMapper.map(kichThuocEditRequest, Response.class);
        kichThuocRepository.save(kichThuocEditRequest);
        return ResponseEntity.accepted().body(kichThuoc);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstKichThuoc) {

        try{
            List<KichThuoc> lstMS = lstKichThuoc.stream()
                    .map((element) -> modelMapper.map(element, KichThuoc.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = kichThuocRepository.saveAll(lstMS).stream()
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
        List<KichThuoc> mauSacList = kichThuocRepository.findAll();
        ExportExcel<KichThuoc> exportExcel = new ExportExcel<KichThuoc> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=kich_thuoc.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
