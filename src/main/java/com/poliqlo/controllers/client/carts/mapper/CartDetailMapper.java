package com.poliqlo.controllers.client.carts.mapper;

import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.GioHangChiTiet;

import java.math.BigDecimal;

public class CartDetailMapper {
    public static CartDetailResponseDTO mapToDTO(GioHangChiTiet cartDetail, DotGiamGia discount, BigDecimal giaSauKhiGiam, String url) {
        CartDetailResponseDTO responseDTO = new CartDetailResponseDTO();
        responseDTO.setId(cartDetail.getId());
        responseDTO.setCustomerId(cartDetail.getKhachHang().getId());
        responseDTO.setProductDetailId(cartDetail.getSanPhamChiTiet().getId());
        responseDTO.setColor(cartDetail.getSanPhamChiTiet().getMauSac().getTen());
        responseDTO.setSize(cartDetail.getSanPhamChiTiet().getKichThuoc().getTen());
        responseDTO.setName(cartDetail.getSanPhamChiTiet().getSanPham().getTen());
        responseDTO.setPrice(String.valueOf(cartDetail.getSanPhamChiTiet().getGiaBan()));
        responseDTO.setQuantity(String.valueOf(cartDetail.getSoLuong()));
        responseDTO.setUrl(url);
        if (discount != null) {
            responseDTO.setPriceAfterDiscount(String.valueOf(giaSauKhiGiam));
            responseDTO.setDiscountId(discount.getId());
        } else {
            responseDTO.setPriceAfterDiscount(String.valueOf(cartDetail.getSanPhamChiTiet().getGiaBan()));
        }
        BigDecimal tongTien = BigDecimal.valueOf(cartDetail.getSoLuong()).multiply(giaSauKhiGiam);
        responseDTO.setTotalPrice(String.valueOf(tongTien));

        return responseDTO;
    }
}
