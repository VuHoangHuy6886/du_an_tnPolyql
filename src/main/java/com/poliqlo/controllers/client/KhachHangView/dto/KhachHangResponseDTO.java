package com.poliqlo.controllers.client.KhachHangView.dto;

import com.poliqlo.models.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KhachHangResponseDTO {

    private Integer id;
    private Integer idTaiKhoan;
    private String ten;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;
    private String gioiTinh;
    private Integer soLanTuChoiNhanHang;
    private String soDienThoai;
    private String email;
    private Boolean isDeleted = false;
}
