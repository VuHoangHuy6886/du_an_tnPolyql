package com.poliqlo.controllers.admin.ban_hang.model.response;

import com.poliqlo.models.DotGiamGia;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.SanPhamChiTiet}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamDetailBanHang implements Serializable {
    @Id
    private Integer id;
    private String kichThuoc;
    private String mauSacId;
    @Transient
    private DotGiamGia dotGiamGia;
    private String soLuong;
}