package com.poliqlo.controllers.common.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO cho {@link com.poliqlo.models.SanPham} - Đại diện cho dữ liệu sản phẩm trả về từ API
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SanPhamAPIResponse implements Serializable {

    /** ID của sản phẩm */
    Integer id;

    /** Thương hiệu của sản phẩm */
    ThuongHieu thuongHieu;

    /** Chất liệu của sản phẩm */
    ChatLieu chatLieu;

    /** Kiểu dáng của sản phẩm */
    KieuDang kieuDang;

    /** Mã sản phẩm */
    String maSanPham;

    /** Tên sản phẩm */
    String ten;

    /** Trạng thái của sản phẩm (ví dụ: Đang bán, Hết hàng, Ngừng kinh doanh) */
    String trangThai;

    /** Mô tả chi tiết về sản phẩm */
    String moTa;

    /** Đường dẫn ảnh đại diện của sản phẩm */
    String anhUrl;

    /** Cờ đánh dấu sản phẩm đã bị xóa hay chưa */
    Boolean isDeleted;

    /** Danh sách các phiên bản chi tiết của sản phẩm (màu sắc, kích thước, giá...) */
    Set<SanPhamChiTiet> sanPhamChiTiets;

    /** Danh mục mà sản phẩm thuộc về */
    Set<DanhMucDto> danhMucs;

    /** Danh sách ảnh của sản phẩm */
    List<AnhDto> anhs;

    /** Tổng số lượng sản phẩm đang có sẵn */
    Integer soLuong = 1;

    /** Giá bán của sản phẩm */
    Integer giaBan = 1;

    /** Đánh dấu sản phẩm có đang trong chương trình khuyến mãi hay không */
    Boolean isPromotionProduct;

    /** DTO cho thương hiệu sản phẩm */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ThuongHieu implements Serializable {
        Integer id;
        String ten;
        String thumbnail; // Đường dẫn ảnh thương hiệu
    }

    /** DTO cho chất liệu sản phẩm */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChatLieu implements Serializable {
        Integer id;
        String ten;
    }

    /** DTO cho kiểu dáng sản phẩm */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class KieuDang implements Serializable {
        Integer id;
        String ten;
    }

    /** DTO cho phiên bản chi tiết của sản phẩm */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SanPhamChiTiet implements Serializable {
        Integer id;
        KichThuocDto kichThuoc;
        MauSacDto mauSac;
        BigDecimal giaBan;
        BigDecimal giaChietKhau = BigDecimal.valueOf(9999999999.0); // Giá chiết khấu nếu có
        Integer soLuong; // Số lượng còn lại trong kho
        String barcode; // Mã vạch sản phẩm
        Boolean isDeleted; // Đánh dấu sản phẩm chi tiết có bị xóa hay không
        DotGiamGiaDto dotGiamGia; // Chương trình giảm giá hiện tại
        Boolean isPromotionProduct = true;

        /** DTO cho kích thước sản phẩm */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        public static class KichThuocDto implements Serializable {
            Integer id;
            String ten;
        }

        /** DTO cho chương trình giảm giá */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        public static class DotGiamGiaDto implements Serializable {
            Integer id;
            String ten;
            private String ma; // Mã khuyến mãi
            private String moTa; // Mô tả chương trình giảm giá
            private LocalDateTime thoiGianBatDau;
            private LocalDateTime thoiGianKetThuc;
            private String loaiChietKhau; // Phần trăm hoặc số tiền cụ thể
            private BigDecimal giaTriGiam; // Giá trị giảm
            private BigDecimal giamToiDa; // Giảm tối đa cho một sản phẩm
            private String trangThai; // Trạng thái khuyến mãi (còn hiệu lực hay không)
        }

        /** DTO cho màu sắc sản phẩm */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        public static class MauSacDto implements Serializable {
            Integer id;
            String ten;
            String code; // Mã hex của màu sắc
        }
    }

    /** DTO cho danh mục sản phẩm */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class DanhMucDto implements Serializable {
        Integer id;
        String ten;
    }

    /** DTO cho ảnh sản phẩm */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class AnhDto implements Serializable {
        Integer id;
        Integer mauSacId; // Liên kết với màu sắc nếu có
        Boolean isDefault; // Ảnh chính hay ảnh phụ
        String url; // Đường dẫn ảnh
        Boolean isDeleted; // Đánh dấu ảnh có bị xóa hay không
    }
}
