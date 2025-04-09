package com.poliqlo.controllers.admin.dot_giam_gia.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailResponseDGG {
    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String kichThuoc;

    private String mauSac;

    private BigDecimal giaBan;

    private String barcode;
}
