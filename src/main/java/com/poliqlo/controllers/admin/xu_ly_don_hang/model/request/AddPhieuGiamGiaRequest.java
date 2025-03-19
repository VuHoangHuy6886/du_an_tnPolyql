package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class AddPhieuGiamGiaRequest {
    private String ten;
    private String soLuong;
    private String hoaDonToiThieu;
    private String loaiHinhGiam;
    private String giaTriGiam;
    private String giamToiDa;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayBatDau;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayKetThuc;
    private String trangThai;
}
