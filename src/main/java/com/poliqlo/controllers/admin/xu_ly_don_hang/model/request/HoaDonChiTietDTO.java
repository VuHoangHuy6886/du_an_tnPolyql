package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietDTO {
    private Integer id;
    private Integer hoaDonId;
    private Integer sanPhamChiTietId;
    private String tenSanPham;
    private BigDecimal giaGoc;
    private BigDecimal giaKhuyenMai;
    private Integer soLuong;
}
