package com.poliqlo.controllers.admin.ban_hang.model.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.poly.polystore.entity.MaGiamGia}
 */
@Data
public class MaGiamGiaDto implements Serializable {
    Integer id;
    String code;
    Double phanTramGiam;
    BigDecimal giamToiDa;
    BigDecimal giaTriToiThieu;
    Integer soLuong;
    Instant thoiGianKetThuc;
    Instant thoiGianBatDau;
    Instant createAt;
    Instant updateAt;
    String lastModifiedBy;
    Boolean deleted;
}