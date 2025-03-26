package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichSuHoaDonDTO {
    private Integer id;
    private String tieuDe;
    private String moTa;
    private LocalDateTime thoiGian;
    private Integer taiKhoanId;
    private String tenTaiKhoan;   // vd: username, hoTen, v.v.

    // Thông tin hóa đơn
    private Integer hoaDonId;
    private String trangThaiHoaDon;
    private Long tongTien;
}
