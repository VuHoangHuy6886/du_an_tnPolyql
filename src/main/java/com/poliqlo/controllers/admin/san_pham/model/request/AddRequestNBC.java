package com.poliqlo.controllers.admin.san_pham.model.request;

import com.poliqlo.validation.Unique;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.poliqlo.models.SanPham}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class AddRequestNBC implements Serializable {
    @PositiveOrZero
    Integer thuongHieuId;
    @PositiveOrZero
    Integer chatLieuId;
    @PositiveOrZero
    Integer kieuDangId;
    @NotNull
    @Size(max = 50)
    @Unique(entityClass = com.poliqlo.models.SanPham.class, fieldName = "maSanPham")
    String maSanPham;
    @NotNull
    @Size(max = 255)
    String ten;
    @Size(max = 50)
    String trangThai;
    String moTa;
    @Size(max = 255)
    String anhUrl;
    Boolean isDeleted;
    @Size(min=1)
    Set<SanPhamChiTiet> sanPhamChiTiets;
    @NotEmpty
    Set<Integer> danhMucIds;
    private List<Anh> anhs = new ArrayList<>();

    /**
     * DTO for {@link com.poliqlo.models.SanPhamChiTiet}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class SanPhamChiTiet implements Serializable {
        @PositiveOrZero
        Integer kichThuocId;
        @PositiveOrZero
        Integer mauSacId;
        @PositiveOrZero
        BigDecimal giaBan;
        @PositiveOrZero
        Integer soLuong;
        @Size(max = 100)
        String barcode;
        Boolean isDeleted;
    }

    /**
     * DTO for {@link com.poliqlo.models.Anh}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class Anh implements Serializable {
        private Boolean isDefault = false;
        @NotEmpty
        private String url;
        @PositiveOrZero
        private Integer mauSacId;
    }
}