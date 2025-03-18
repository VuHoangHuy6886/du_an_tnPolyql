package com.poliqlo.controllers.common.api.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.poliqlo.models.SanPhamChiTiet}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamChiTietAPIResponse implements Serializable {
    Integer id;
    @NotNull
    SanPhamChiTietAPIResponse.SanPhamDto sanPham;
    KichThuocDto kichThuoc;
    MauSacDto mauSac;
    @NotNull
    BigDecimal giaBan;
    BigDecimal giaChietKhau;
    @NotNull
    Integer soLuong;
    @Size(max = 100)
    String barcode;
    String ten;
    @NotNull
    Boolean isDeleted;
    List<DotGiamGiaDto> dotGiamGias;
    DotGiamGiaDto dotGiamGia;

    /**
     * DTO for {@link com.poliqlo.models.SanPham}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SanPhamDto implements Serializable {
        Integer id;
        ThuongHieuDto thuongHieu;
        ChatLieuDto chatLieu;
        KieuDangDto kieuDang;
        @NotNull
        @Size(max = 50)
        String maSanPham;
        @NotNull
        @Size(max = 255)
        String ten;
        @Size(max = 50)
        String trangThai;
        String moTa;
        @Size(max = 255)
        String anhUrl;
        @NotNull
        Boolean isDeleted;
        List<AnhDto> anhs;

        /**
         * DTO for {@link com.poliqlo.models.ThuongHieu}
         */
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ThuongHieuDto implements Serializable {
            Integer id;
            @NotNull
            @Size(max = 255)
            String ten;
            @Size(max = 255)
            String thumbnail;
        }

        /**
         * DTO for {@link com.poliqlo.models.ChatLieu}
         */
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class ChatLieuDto implements Serializable {
            Integer id;
            @NotNull
            @Size(max = 255)
            String ten;
        }

        /**
         * DTO for {@link com.poliqlo.models.KieuDang}
         */
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class KieuDangDto implements Serializable {
            Integer id;
            @NotNull
            @Size(max = 255)
            String ten;
        }

        /**
         * DTO for {@link com.poliqlo.models.Anh}
         */
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @Setter
        public static class AnhDto implements Serializable {
            Integer id;
            Integer mauSacId;
            @NotNull
            Boolean isDefault;
            @NotNull
            @Size(max = 255)
            String url;
            Boolean isDeleted;
        }
    }

    /**
     * DTO for {@link com.poliqlo.models.KichThuoc}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class KichThuocDto implements Serializable {
        Integer id;
        @NotNull
        @Size(max = 50)
        String ten;
    }

    /**
     * DTO for {@link com.poliqlo.models.MauSac}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class MauSacDto implements Serializable {
        Integer id;
        @NotNull
        @Size(max = 50)
        String ten;
        @Size(max = 10)
        String code;
    }

    /**
     * DTO for {@link com.poliqlo.models.DotGiamGia}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DotGiamGiaDto implements Serializable {
        Integer id;
        @NotNull
        @Size(max = 50)
        String ma;
        @NotNull
        @Size(max = 255)
        String ten;
        String moTa;
        @NotNull
        LocalDateTime thoiGianBatDau;
        @NotNull
        LocalDateTime thoiGianKetThuc;
        @Size(max = 50)
        String loaiChietKhau;
        BigDecimal giaTriGiam;
        BigDecimal giamToiDa;
        @Size(max = 50)
        String trangThai;
        @NotNull
        Boolean isDeleted;
    }
}