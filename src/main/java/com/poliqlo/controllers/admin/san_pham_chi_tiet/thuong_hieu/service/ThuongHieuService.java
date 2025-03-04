package com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.service;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.response.Response;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.ThuongHieu;
import com.poliqlo.repositories.ThuongHieuRepository;
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
public class ThuongHieuService {
    private final ModelMapper modelMapper;
    private final ThuongHieuRepository thuongHieuRepository;
    private final BlobStoreService blobStoreService;


    public ResponseEntity<Response> save(@Valid AddRequest req) throws IOException {
        ThuongHieu thuongHieu = modelMapper.map(req, ThuongHieu.class);
        var blobResp = blobStoreService.upload(req.getThumbnail());
        thuongHieu.setThumbnail(blobResp.getUrl());
        Response thuongHieuResponse = modelMapper.map(thuongHieuRepository.save(thuongHieu), Response.class);
        return new ResponseEntity<>(thuongHieuResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) throws IOException {
        var oldThuongHieu = thuongHieuRepository.findById(editReq.getId()).get();
        ThuongHieu thuongHieu = modelMapper.map(editReq, ThuongHieu.class);
        if (editReq.getThumbnail().isEmpty()) {
            thuongHieu.setThumbnail(oldThuongHieu.getThumbnail());
        }else{
            var blobResp = blobStoreService.upload(editReq.getThumbnail());
            thuongHieu.setThumbnail(blobResp.getUrl());
            blobStoreService.delete(oldThuongHieu.getThumbnail());
        }


        thuongHieuRepository.save(thuongHieu);

        var resp = thuongHieuRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<ThuongHieu> delete(Integer id) {
        if (id == null || !thuongHieuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var thuongHieuEditRequest = thuongHieuRepository.findById(id).get();
        thuongHieuEditRequest.setIsDeleted(true);
        var thuongHieu = modelMapper.map(thuongHieuEditRequest, Response.class);
        var resp = thuongHieuRepository.save(thuongHieuEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !thuongHieuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var thuongHieuEditRequest = thuongHieuRepository.findById(id).get();
        thuongHieuEditRequest.setIsDeleted(false);
        var thuongHieu = modelMapper.map(thuongHieuEditRequest, Response.class);
        thuongHieuRepository.save(thuongHieuEditRequest);
        return ResponseEntity.accepted().body(thuongHieu);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstThuongHieu) {

        try {
            List<ThuongHieu> lstTH = lstThuongHieu.stream()
                    .map((element) -> modelMapper.map(element, ThuongHieu.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = thuongHieuRepository.saveAll(lstTH).stream()
                    .map((element) -> modelMapper.map(element, Response.class))
                    .collect(Collectors.toList());
            return ResponseEntity.accepted().body(lstRP);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
    }

    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<ThuongHieu> thuongHieuList = thuongHieuRepository.findAll();
        ExportExcel<ThuongHieu> exportExcel = new ExportExcel<ThuongHieu>();
        ByteArrayInputStream in = exportExcel.export(thuongHieuList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=thuong_hieu.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
