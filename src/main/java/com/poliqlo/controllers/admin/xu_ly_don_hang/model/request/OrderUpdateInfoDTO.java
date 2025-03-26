package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateInfoDTO {
    private String tenNguoiNhan;
    private String diaChi;
    private String soDienThoai;
    private BigDecimal phiVanChuyen;
    private String ghiChu;
}