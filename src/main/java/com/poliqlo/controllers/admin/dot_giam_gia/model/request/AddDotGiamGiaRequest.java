package com.poliqlo.controllers.admin.dot_giam_gia.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddDotGiamGiaRequest {

    @NotBlank(message = "Tên không được để trống")
    private String ten;

    private String moTa;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianKetThuc;

    private String loaiChietKhau;

//    @NotBlank(message = "Giá trị giảm không được để trống")
//    @Pattern(regexp = "\\d+", message = "Giá trị giảm chỉ được nhập số, không được nhập chữ hoặc ký tự đặc biệt")
    private String giaTriGiam;

    @NotBlank(message = "Giảm tối đa không được để trống")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Giảm tối đa chỉ được nhập số nguyên dương")
    private String giamToiDa;

//    @NotBlank(message = "Trạng thái không được để trống")
//    private String trangThai;
}
