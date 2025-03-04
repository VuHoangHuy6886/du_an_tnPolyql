package com.poliqlo.controllers.admin.nhan_vien.service;

import com.poliqlo.controllers.admin.nhan_vien.model.request.AddRequest;
import com.poliqlo.controllers.admin.nhan_vien.model.request.EditReq;
import com.poliqlo.controllers.admin.nhan_vien.model.request.ImportReq;
import com.poliqlo.controllers.admin.nhan_vien.model.response.Response;
import com.poliqlo.models.NhanVien;
import com.poliqlo.repositories.NhanVienRepository;
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
public class NhanVienService {
    private final ModelMapper modelMapper;
    private final NhanVienRepository nhanVienRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        NhanVien nhanVien = modelMapper.map(req, NhanVien.class);
        Response nhanVienResponse = modelMapper.map(nhanVienRepository.save(nhanVien), Response.class);
        return new ResponseEntity<>(nhanVienResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        nhanVienRepository.save(modelMapper.map(editReq, NhanVien.class));
        var resp= nhanVienRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<NhanVien> delete(Integer id) {
        if (id == null || !nhanVienRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var nhanVienEditRequest = nhanVienRepository.findById(id).get();
        nhanVienEditRequest.setIsDeleted(true);
        var nhanVien = modelMapper.map(nhanVienEditRequest, Response.class);
        var resp=nhanVienRepository.save(nhanVienEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !nhanVienRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var nhanVienEditRequest = nhanVienRepository.findById(id).get();
        nhanVienEditRequest.setIsDeleted(false);
        var nhanVien = modelMapper.map(nhanVienEditRequest, Response.class);
        nhanVienRepository.save(nhanVienEditRequest);
        return ResponseEntity.accepted().body(nhanVien);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstNhanVien) {

        try{
            List<NhanVien> lstMS = lstNhanVien.stream()
                    .map((element) -> modelMapper.map(element, NhanVien.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = nhanVienRepository.saveAll(lstMS).stream()
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
        List<NhanVien> mauSacList = nhanVienRepository.findAll();
        ExportExcel<NhanVien> exportExcel = new ExportExcel<NhanVien> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=nhan_vien.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

    public List<Response> findAll() {
        return nhanVienRepository.findAll().stream()
               .map(nhanVien -> {
                   Response response = modelMapper.map(nhanVien, Response.class);
                   return response;
               })
               .collect(Collectors.toList());
    }
}
