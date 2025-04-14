package com.poliqlo.controllers.admin.hoa_don.model;

import com.poliqlo.controllers.admin.hoa_don.service.DiaLyService;
import com.poliqlo.models.DiaChi;
import lombok.Data;

@Data
public class DiaChiDTO {
    private String diaChiChiTiet;
    private String phuongXa;
    private String quanHuyen;
    private String tinhThanhPho;
    private String diaChiDayDu;

    public static DiaChiDTO fromEntity(DiaChi diaChi, DiaLyService diaLyService) {
        DiaChiDTO dto = new DiaChiDTO();
        dto.setDiaChiChiTiet(diaChi.getAddress());
        // Sửa lại: Truyền thêm districtId vào layTenPhuongXa
        dto.setPhuongXa(diaLyService.layTenPhuongXa(diaChi.getWardCode(), diaChi.getDistrictId()));
        dto.setQuanHuyen(diaLyService.layTenQuanHuyen(diaChi.getDistrictId()));
        dto.setTinhThanhPho(diaLyService.layTenTinhThanhPho(diaChi.getProvinceId()));

        String diaChiDayDu = diaLyService.ghepDiaChiDayDu(
                diaChi.getAddress(),
                diaChi.getWardCode(),
                diaChi.getDistrictId(),
                diaChi.getProvinceId());
        dto.setDiaChiDayDu(diaChiDayDu);
        return dto;
    }
}
