package com.poliqlo.controllers.client.KhachHangView.mapper;

import com.poliqlo.controllers.client.KhachHangView.dto.KhachHangResponseDTO;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.TaiKhoan;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

public class KhachHangMapper {
    public static KhachHangResponseDTO KhachHangToResponse(KhachHang khachHang) {
        KhachHangResponseDTO responseDTO = KhachHangResponseDTO.builder()
                .id(khachHang.getId())
                .gioiTinh(khachHang.getGioiTinh())
                .ten(khachHang.getTen())
                .soLanTuChoiNhanHang(khachHang.getSoLanTuChoiNhanHang())
                .idTaiKhoan(khachHang.getTaiKhoan().getId())
                .ngaySinh(khachHang.getNgaySinh())
                .soDienThoai(khachHang.getTaiKhoan().getSoDienThoai())
                .email(khachHang.getTaiKhoan().getEmail())
                .build();
        return responseDTO;
    }

    public static KhachHang responseToEntityCustomer(KhachHangResponseDTO responseDTO, TaiKhoan taiKhoan) {
        KhachHang khachHang = new KhachHang();
        khachHang.setTaiKhoan(taiKhoan);
        khachHang.setId(responseDTO.getId());
        khachHang.setTen(responseDTO.getTen());
        khachHang.setGioiTinh(responseDTO.getGioiTinh());
        khachHang.setNgaySinh(responseDTO.getNgaySinh());
        return khachHang;
    }
}
