package com.poliqlo.controllers.admin.san_pham.service;

import com.poliqlo.controllers.admin.san_pham.model.request.AddRequestNBC;
import com.poliqlo.models.*;
import com.poliqlo.repositories.SanPhamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamService {
    private final SanPhamRepository sanPhamRepository;
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
}
