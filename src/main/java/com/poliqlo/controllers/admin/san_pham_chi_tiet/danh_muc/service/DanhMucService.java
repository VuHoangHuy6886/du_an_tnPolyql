package com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.service;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.danh_muc.model.response.Response;
import com.poliqlo.models.DanhMuc;
import com.poliqlo.repositories.DanhMucRepository;
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
public class DanhMucService {
    private final ModelMapper modelMapper;
    private final DanhMucRepository danhMucRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        DanhMuc danhMuc = modelMapper.map(req, DanhMuc.class);
        Response danhMucResponse = modelMapper.map(danhMucRepository.save(danhMuc), Response.class);
        return new ResponseEntity<>(danhMucResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        danhMucRepository.save(modelMapper.map(editReq, DanhMuc.class));
        var resp= danhMucRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<DanhMuc> delete(Integer id) {
        if (id == null || !danhMucRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var danhMucEditRequest = danhMucRepository.findById(id).get();
        danhMucEditRequest.setIsDeleted(true);
        var danhMuc = modelMapper.map(danhMucEditRequest, Response.class);
        var resp=danhMucRepository.save(danhMucEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !danhMucRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var danhMucEditRequest = danhMucRepository.findById(id).get();
        danhMucEditRequest.setIsDeleted(false);
        var danhMuc = modelMapper.map(danhMucEditRequest, Response.class);
        danhMucRepository.save(danhMucEditRequest);
        return ResponseEntity.accepted().body(danhMuc);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstDanhMuc) {

        try{
            List<DanhMuc> lstMS = lstDanhMuc.stream()
                    .map((element) -> modelMapper.map(element, DanhMuc.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = danhMucRepository.saveAll(lstMS).stream()
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
        List<DanhMuc> mauSacList = danhMucRepository.findAll();
        ExportExcel<DanhMuc> exportExcel = new ExportExcel<DanhMuc> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=danh_muc.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
