package com.poliqlo.controllers.admin.san_pham.service;

import com.poliqlo.controllers.admin.san_pham.model.request.AddRequest;
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
    public ResponseEntity<?> persist(AddRequest addRequest){
        var sp = SanPham.builder()
                .maSanPham(addRequest.getMaSanPham())
                .ten(addRequest.getTen())
                .moTa(addRequest.getMoTa())
                .trangThai(addRequest.getTrangThai())
                .thuongHieu(ThuongHieu.builder().id(addRequest.getThuongHieuId()).build())
                .danhMucs(addRequest.getDanhMucIds().stream().map(id -> DanhMuc.builder().id(id).build()).collect(Collectors.toSet()))
                .kieuDang(KieuDang.builder().id(addRequest.getKieuDangId()).build())
                .chatLieu(ChatLieu.builder().id(addRequest.getChatLieuId()).build())
                .trangThai(addRequest.getTrangThai())
                .anhUrl(addRequest.getAnhUrl())
                .isDeleted(false)
                .build();
        sp.setAnhs(addRequest.getAnhs().stream()
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
        sp.setSanPhamChiTiets(addRequest.getSanPhamChiTiets().stream()
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
