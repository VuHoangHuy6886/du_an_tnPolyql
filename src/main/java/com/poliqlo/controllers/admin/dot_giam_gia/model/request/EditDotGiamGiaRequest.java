package com.poliqlo.controllers.admin.dot_giam_gia.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EditDotGiamGiaRequest {

    private Integer id;

    private String ma;

    @NotBlank(message = "Tên không được để trống")
    private String ten;

    @NotBlank(message = "Mô tả không được để trống")
    private String moTa;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianBatDau;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime thoiGianKetThuc;

    @NotBlank(message = "Loại chiết khấu không được để trống")
    private String loaiChietKhau;

    @NotBlank(message = "Giá trị giảm không được để trống")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Giá trị giảm phải là số nguyên dương")
    private String giaTriGiam;

    @NotBlank(message = "Giảm tối đa không được để trống")
    @Pattern(regexp = "^[1-9]\\d*$", message = "Giảm tối đa phải là số nguyên dương")
    private String giamToiDa;

    @NotBlank(message = "Trạng thái không được để trống")
    private String trangThai;

    private Boolean isDeleted = false;
}
