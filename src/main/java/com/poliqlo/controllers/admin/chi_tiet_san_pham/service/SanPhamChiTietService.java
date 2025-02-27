package com.poliqlo.controllers.admin.chi_tiet_san_pham.service;

import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request.AddRequest;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request.EditReq;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request.ImportReq;
import com.poliqlo.controllers.admin.chi_tiet_san_pham.model.response.Response;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.SanPhamChiTietRepository;
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
public class SanPhamChiTietService {
    private final ModelMapper modelMapper;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        SanPhamChiTiet chatLieu = modelMapper.map(req, SanPhamChiTiet.class);
        Response chatLieuResponse = modelMapper.map(sanPhamChiTietRepository.save(chatLieu), Response.class);
        return new ResponseEntity<>(chatLieuResponse, HttpStatus.CREATED);
    }

//    public ResponseEntity<Response> update(@Valid EditReq editReq) {
//        sanPhamChiTietRepository.save(modelMapper.map(editReq, SanPhamChiTiet.class));
//        var resp= sanPhamChiTietRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
//        return ResponseEntity.accepted().body(resp);
//    }

    public ResponseEntity<SanPhamChiTiet> delete(Integer id) {
        if (id == null || !sanPhamChiTietRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var chatLieuEditRequest = sanPhamChiTietRepository.findById(id).get();
        chatLieuEditRequest.setIsDeleted(true);
        var chatLieu = modelMapper.map(chatLieuEditRequest, Response.class);
        var resp=sanPhamChiTietRepository.save(chatLieuEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !sanPhamChiTietRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var chatLieuEditRequest = sanPhamChiTietRepository.findById(id).get();
        chatLieuEditRequest.setIsDeleted(false);
        var chatLieu = modelMapper.map(chatLieuEditRequest, Response.class);
        sanPhamChiTietRepository.save(chatLieuEditRequest);
        return ResponseEntity.accepted().body(chatLieu);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstSanPhamChiTiet) {

        try{
            List<SanPhamChiTiet> lstMS = lstSanPhamChiTiet.stream()
                    .map((element) -> modelMapper.map(element, SanPhamChiTiet.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = sanPhamChiTietRepository.saveAll(lstMS).stream()
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
        List<SanPhamChiTiet> mauSacList = sanPhamChiTietRepository.findAll();
        ExportExcel<SanPhamChiTiet> exportExcel = new ExportExcel<SanPhamChiTiet> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=chat_lieu.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
