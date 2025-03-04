package com.poliqlo.controllers.admin.dot_giam_gia.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter

public class DotGiamGiaResponse {
    private Integer id;

    private String ma;

    private String ten;

    private String moTa;

    private Instant thoiGianBatDau;

    private Instant thoiGianKetThuc;

    private String loaiChietKhau;

    private BigDecimal giaTriGiam;

    private BigDecimal giamToiDa;

    private String trangThai;

    private Boolean isDeleted = false;
}
