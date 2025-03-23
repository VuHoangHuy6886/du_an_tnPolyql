package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductDetailDTO {
    private Integer idSanPhamChiTiet;
    private Integer soLuong;
    private BigDecimal giaGoc;
    private BigDecimal giaKhuyenMai;
}