package com.poliqlo.controllers.admin.ban_hang.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.poliqlo.models.PhieuGiamGia}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhieuGiamGiaDto implements Serializable,Select2Response {

    Integer id;
    @NotNull
    @Size(max = 50)
    String ma;
    @NotNull
    @Size(max = 255)
    String ten;
    @NotNull
    Integer soLuong;
    BigDecimal hoaDonToiThieu;
    @Size(max = 50)
    String loaiHinhGiam;
    BigDecimal giaTriGiam;
    BigDecimal giamToiDa;
    @NotNull
    LocalDateTime ngayBatDau;
    @NotNull
    LocalDateTime ngayKetThuc;
    @Size(max = 50)
    String trangThai;
    @NotNull
    Boolean isDeleted;

    @Override
    public String getText() {
        return ma;
    }
    @Override
    public String getId() {
        return id+"";
    }
}