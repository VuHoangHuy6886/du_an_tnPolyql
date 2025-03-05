package com.poliqlo.controllers.admin.san_pham.service;

import com.poliqlo.controllers.admin.san_pham.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham.model.request.AddRequestNBC;
import com.poliqlo.controllers.admin.san_pham.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham.model.response.Response;
import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import com.poliqlo.utils.ExportExcel;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamService {
    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;
    private final ThuongHieuRepository thuongHieuRepository;

    private final ChatLieuRepository chatLieuRepository;
    private final DanhMucRepository DanhMucRepository;

    private final KieuDangRepository kieuDangRepository;
    @Transactional
    public ResponseEntity<?> persist(AddRequestNBC addRequestNBC){
        var sp = SanPham.builder()
                .maSanPham(addRequestNBC.getMaSanPham())
                .ten(addRequestNBC.getTen())
                .moTa(addRequestNBC.getMoTa())
                .trangThai(addRequestNBC.getTrangThai())
                .thuongHieu(ThuongHieu.builder().id(addRequestNBC.getThuongHieuId()).build())
                .danhMucs(addRequestNBC.getDanhMucIds().stream().map(id -> DanhMuc.builder().id(id).build()).collect(Collectors.toSet()))
                .kieuDang(KieuDang.builder().id(addRequestNBC.getKieuDangId()).build())
                .chatLieu(ChatLieu.builder().id(addRequestNBC.getChatLieuId()).build())
                .trangThai(addRequestNBC.getTrangThai())
                .anhUrl(addRequestNBC.getAnhUrl())
                .isDeleted(false)
                .build();
        sp.setAnhs(addRequestNBC.getAnhs().stream()
                .map(obj -> {
                    var anh = Anh.builder()
                            .sanPham(sp)
                            .url(obj.getUrl())
                            .mauSac(MauSac.builder().id(obj.getMauSacId()).build())
                            .isDefault(obj.getIsDefault())
                            .isDeleted(false)
                            .build();
                    return anh;
                })
                .toList()
        );
        sp.setSanPhamChiTiets(addRequestNBC.getSanPhamChiTiets().stream()
                .map(spct->{
                    var spctNew=SanPhamChiTiet.builder()
                            .sanPham(sp)
                            .barcode(spct.getBarcode())
                            .giaBan(spct.getGiaBan())
                            .kichThuoc(KichThuoc.builder().id(spct.getKichThuocId()).build())
                            .mauSac(MauSac.builder().id(spct.getMauSacId()).build())
                            .isDeleted(false)
                            .soLuong(spct.getSoLuong())
                            .build();
                    return spctNew;
                })
                .collect(Collectors.toSet())
        );
        var resp=sanPhamRepository.save(sp);
        return ResponseEntity.ok(sp);
    }
    public boolean updateSanPham(EditReq dto) {
        int updatedCount = sanPhamRepository.updateSanPham(
                dto.getId(),
                dto.getMaSanPham(),
                dto.getTen(),
                dto.getIdThuongHieu(),
                dto.getIdChatLieu(),
                dto.getIdKieuDang()
        );
        return updatedCount > 0;
    }


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        SanPham chatLieu = modelMapper.map(req, SanPham.class);
        Response sanPhamResponse = modelMapper.map(sanPhamRepository.save(chatLieu), Response.class);
        return new ResponseEntity<>(sanPhamResponse, HttpStatus.CREATED);
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
