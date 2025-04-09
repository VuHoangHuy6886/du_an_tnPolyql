package com.poliqlo.controllers.admin.dot_giam_gia.Mapper;

import com.poliqlo.controllers.admin.dot_giam_gia.model.request.AddDotGiamGiaRequest;
import com.poliqlo.controllers.admin.dot_giam_gia.model.request.EditDotGiamGiaRequest;
import com.poliqlo.controllers.admin.dot_giam_gia.model.response.ProductDetailResponseDGG;
import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.utils.DiscountStatusUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class DotGiamGiaMapper {
    public DotGiamGia toDotGiamGia(AddDotGiamGiaRequest request, String genMa) {
        BigDecimal giaTriGiam = Optional.ofNullable(request.getGiaTriGiam())
                .filter(str -> !str.trim().isEmpty())
                .map(BigDecimal::new)
                .orElse(null);

        BigDecimal giamToiDa = Optional.ofNullable(request.getGiamToiDa())
                .filter(str -> !str.trim().isEmpty())
                .map(BigDecimal::new)
                .orElse(null);
        String motaUpdate = null;
        if (request.getMoTa() != null && !request.getMoTa().trim().isEmpty()) {
            motaUpdate = request.getMoTa();
        }
        return DotGiamGia.builder()
                .ma(genMa)
                .ten(request.getTen())
                .moTa(motaUpdate)
                .thoiGianBatDau(request.getThoiGianBatDau())
                .thoiGianKetThuc(request.getThoiGianKetThuc())
                .loaiChietKhau(request.getLoaiChietKhau())
                .giaTriGiam(giaTriGiam)
                .giamToiDa(giamToiDa)
                .trangThai(DiscountStatusUtil.getStatus(request.getThoiGianBatDau(), request.getThoiGianKetThuc(), false))
                .isDeleted(false)
                .build();
    }


    public DotGiamGia EditToDotGiamGia(EditDotGiamGiaRequest request) {
        BigDecimal giaTriGiam = Optional.ofNullable(request.getGiaTriGiam())
                .filter(str -> !str.trim().isEmpty())
                .map(BigDecimal::new)
                .orElse(null);

        BigDecimal giamToiDa = Optional.ofNullable(request.getGiamToiDa())
                .filter(str -> !str.trim().isEmpty())
                .map(BigDecimal::new)
                .orElse(null);

        return DotGiamGia.builder()
                .id(request.getId())
                .ma(request.getMa())
                .ten(request.getTen())
                .moTa(request.getMoTa())
                .thoiGianBatDau(request.getThoiGianBatDau())
                .thoiGianKetThuc(request.getThoiGianKetThuc())
                .loaiChietKhau(request.getLoaiChietKhau())
                .giaTriGiam(giaTriGiam)
                .giamToiDa(giamToiDa)
                .trangThai(DiscountStatusUtil.getStatus(request.getThoiGianBatDau(), request.getThoiGianKetThuc(), false))
                .isDeleted(false)
                .build();
    }


    public EditDotGiamGiaRequest dotGiamGiaToEditDotGiamGiaRequest(DotGiamGia dotGiamGia) {
        String motaUpdate = null;
        if (dotGiamGia.getMoTa() != null && !dotGiamGia.getMoTa().trim().isEmpty()) {
            motaUpdate = dotGiamGia.getMoTa();
        }
        EditDotGiamGiaRequest request = EditDotGiamGiaRequest.builder()
                .id(dotGiamGia.getId())
                .ma(dotGiamGia.getMa())
                .ten(dotGiamGia.getTen())
                .giaTriGiam(dotGiamGia.getGiaTriGiam() != null ? dotGiamGia.getGiaTriGiam().stripTrailingZeros().toPlainString() : null)
                .loaiChietKhau(dotGiamGia.getLoaiChietKhau())
                .giamToiDa(dotGiamGia.getGiamToiDa() != null ? dotGiamGia.getGiamToiDa().stripTrailingZeros().toPlainString() : null)
                .thoiGianBatDau(dotGiamGia.getThoiGianBatDau())
                .thoiGianKetThuc(dotGiamGia.getThoiGianKetThuc())
                .moTa(dotGiamGia.getMoTa())
                .trangThai(dotGiamGia.getTrangThai())
                .build();

        return request;
    }

    // map sản phẩm chi tiết sang productdetail view
    public static ProductDetailResponseDGG sanPhamChiTietToResponseDGG(SanPhamChiTiet sanPhamChiTiet) {
        ProductDetailResponseDGG productDetailResponseDGG = ProductDetailResponseDGG.builder()
                .id(sanPhamChiTiet.getId())
                .tenSanPham(sanPhamChiTiet.getSanPham().getTen())
                .barcode(sanPhamChiTiet.getBarcode())
                .maSanPham(sanPhamChiTiet.getSanPham().getMaSanPham())
                .mauSac(sanPhamChiTiet.getMauSac().getTen())
                .kichThuoc(sanPhamChiTiet.getKichThuoc().getTen())
                .giaBan(sanPhamChiTiet.getGiaBan())
                .build();
        return productDetailResponseDGG;
    }
}
