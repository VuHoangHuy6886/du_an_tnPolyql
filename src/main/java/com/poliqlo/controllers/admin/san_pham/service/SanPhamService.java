package com.poliqlo.controllers.admin.san_pham.service;

import com.poliqlo.controllers.admin.san_pham.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham.model.response.Response;
import com.poliqlo.models.SanPham;
import com.poliqlo.repositories.ChatLieuRepository;
import com.poliqlo.repositories.KieuDangRepository;
import com.poliqlo.repositories.SanPhamRepository;
import com.poliqlo.repositories.ThuongHieuRepository;
import com.poliqlo.utils.ExportExcel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamService {
    private final ModelMapper modelMapper;
    private final SanPhamRepository sanPhamRepository;
    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private KieuDangRepository kieuDangRepository;

    public SanPham updateSanPham(EditReq dto) {
        // Tìm sản phẩm trong DB
        Optional<SanPham> optional = sanPhamRepository.findById(dto.getId());
        if (optional.isPresent()) {
            SanPham sp = optional.get();

            sp.setMaSanPham(dto.getMaSanPham());
            sp.setTen(dto.getTen());
            sp.setTrangThai(dto.getTrangThai());
            sp.setAnhUrl(dto.getAnhUrl());
            // sp.setMoTa(dto.getMoTa()); // nếu bạn muốn cập nhật mô tả

            sp.setIdThuongHieu(
                    thuongHieuRepository.findById(dto.getIdThuongHieu())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu!"))
            );
            sp.setIdChatLieu(chatLieuRepository.findById(dto.getIdChatLieu())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu!")));
            sp.setIdKieuDang(kieuDangRepository.findById(dto.getIdKieuDang())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy kiểu dáng!")));

            // Lưu lại
            return sanPhamRepository.save(sp);
        }
        return null; // Hoặc throw exception
    }
    public ResponseEntity<Response> save(@Valid AddRequest req) {
        SanPham chatLieu = modelMapper.map(req, SanPham.class);
        Response sanPhamResponse = modelMapper.map(sanPhamRepository.save(chatLieu), Response.class);
        return new ResponseEntity<>(sanPhamResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        sanPhamRepository.save(modelMapper.map(editReq, SanPham.class));
        var resp= sanPhamRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<SanPham> delete(Integer id) {
        if (id == null || !sanPhamRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var chatLieuEditRequest = sanPhamRepository.findById(id).get();
        chatLieuEditRequest.setIsDeleted(true);
        var chatLieu = modelMapper.map(chatLieuEditRequest, Response.class);
        var resp=sanPhamRepository.save(chatLieuEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !sanPhamRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var chatLieuEditRequest = sanPhamRepository.findById(id).get();
        chatLieuEditRequest.setIsDeleted(false);
        var chatLieu = modelMapper.map(chatLieuEditRequest, Response.class);
        sanPhamRepository.save(chatLieuEditRequest);
        return ResponseEntity.accepted().body(chatLieu);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstSanPham) {

        try{
            List<SanPham> lstMS = lstSanPham.stream()
                    .map((element) -> modelMapper.map(element, SanPham.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = sanPhamRepository.saveAll(lstMS).stream()
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
        List<SanPham> mauSacList = sanPhamRepository.findAll();
        ExportExcel<SanPham> exportExcel = new ExportExcel<SanPham> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=chat_lieu.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
