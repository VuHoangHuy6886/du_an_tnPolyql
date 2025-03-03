package com.poliqlo.controllers.admin.san_pham.model.reponse;

import com.poliqlo.models.DanhMuc;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DTO for {@link SanPham}
 */



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class SanPhamDataTable implements Serializable {
    private Integer id;

    private String anhSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private String danhSachMauSac;
    private String danhSachRom;
    private Integer soLuong;
    private String thoiGianBaoHanh;
    private String trangThai;
    private Set<SanPhamChiTietDto> sanPhamChiTiets = new LinkedHashSet<>();
    private Set<DanhMuc> danhMucs = new LinkedHashSet<>();

    /**
     * DTO for {@link com.poliqlo.models.SanPhamChiTiet}
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SanPhamChiTietDto implements Serializable {
        private Integer id;
        private Integer kichThuocId;
        private Integer mauSacId;
        @NotNull
        private BigDecimal giaBan;
        @NotNull
        private Integer soLuong;
        @Size(max = 100)
        private String barcode;
    }
}