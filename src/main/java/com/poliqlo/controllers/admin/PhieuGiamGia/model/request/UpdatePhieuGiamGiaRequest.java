package com.poliqlo.controllers.admin.PhieuGiamGia.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdatePhieuGiamGiaRequest {

    private Integer id;

    private String ma;

    @NotBlank(message = "Ten khong duoc de trong")
    private String ten;

    @NotBlank(message = "So luong Khong duoc de trong")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Chi duoc nhap so nguyen duong")
    private String soLuong;

    @NotBlank(message = "Hoa don toi thieu Khong duoc de trong")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Chi duoc nhap so nguyen duong")
    private String hoaDonToiThieu;

    private String loaiHinhGiam;

    @NotBlank(message = "Gia tri giam Khong duoc de trong")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Chi duoc nhap so nguyen duong")
    private String giaTriGiam;

    @NotBlank(message = "Giam toi da Khong duoc de trong")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Chi duoc nhap so nguyen duong")
    private String giamToiDa;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayBatDau;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayKetThuc;


    private String trangThai;

}
