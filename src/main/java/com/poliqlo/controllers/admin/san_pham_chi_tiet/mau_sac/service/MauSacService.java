package com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.service;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.mau_sac.model.response.Response;
import com.poliqlo.models.MauSac;
import com.poliqlo.repositories.MauSacRepository;
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
public class MauSacService {
    private final ModelMapper modelMapper;
    private final MauSacRepository mauSacRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        MauSac mauSac = modelMapper.map(req, MauSac.class);
        Response mauSacResponse = modelMapper.map(mauSacRepository.save(mauSac), Response.class);
        return new ResponseEntity<>(mauSacResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        mauSacRepository.save(modelMapper.map(editReq, MauSac.class));
        var resp= mauSacRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<MauSac> delete(Integer id) {
        if (id == null || !mauSacRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var mauSacEditRequest = mauSacRepository.findById(id).get();
        mauSacEditRequest.setIsDeleted(true);
        var mauSac = modelMapper.map(mauSacEditRequest, Response.class);
        var resp=mauSacRepository.save(mauSacEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !mauSacRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var mauSacEditRequest = mauSacRepository.findById(id).get();
        mauSacEditRequest.setIsDeleted(false);
        var mauSac = modelMapper.map(mauSacEditRequest, Response.class);
        mauSacRepository.save(mauSacEditRequest);
        return ResponseEntity.accepted().body(mauSac);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstMauSac) {

        try{
            List<MauSac> lstMS = lstMauSac.stream()
                    .map((element) -> modelMapper.map(element, MauSac.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = mauSacRepository.saveAll(lstMS).stream()
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
        List<MauSac> mauSacList = mauSacRepository.findAll();
        ExportExcel<MauSac> exportExcel = new ExportExcel<MauSac> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=mau_sac.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
