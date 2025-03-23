package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

public class ProductOrder {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO.IDENTITY)
    private Integer id;

    private String ten;
    private BigDecimal gia;

    // Thêm trường tồn kho
    private Integer soLuongTon;
}
