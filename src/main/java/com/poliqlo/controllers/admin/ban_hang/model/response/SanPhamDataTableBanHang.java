package com.poliqlo.controllers.admin.ban_hang.model.response;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamDataTableBanHang implements Serializable {
    @Id
    private Integer id;
    private String anhUrl;
    private String tenSanPham;
    private String trangThai;
    private Integer soLuong;

}