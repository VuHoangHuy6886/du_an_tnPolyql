package com.poliqlo.controllers.client.carts.mapper;

import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.GioHangChiTiet;

import java.math.BigDecimal;

public class CartDetailMapper {
    public static CartDetailResponseDTO mapToDTO(GioHangChiTiet cartDetail, DotGiamGia discount, BigDecimal giaSauKhiGiam) {
        CartDetailResponseDTO responseDTO = new CartDetailResponseDTO();
        responseDTO.setCustomerId(String.valueOf(cartDetail.getKhachHang().getId()));
        responseDTO.setProductId(String.valueOf(cartDetail.getSanPhamChiTiet().getId()));
        responseDTO.setColor(cartDetail.getSanPhamChiTiet().getMauSac().getTen());
        responseDTO.setSize(cartDetail.getSanPhamChiTiet().getKichThuoc().getTen());
        responseDTO.setName(cartDetail.getSanPhamChiTiet().getSanPham().getTen());
        responseDTO.setPrice(String.valueOf(cartDetail.getSanPhamChiTiet().getGiaBan()));
        responseDTO.setQuantity(String.valueOf(cartDetail.getSoLuong()));

        if (discount != null) {
            responseDTO.setPriceAfterDiscount(String.valueOf(giaSauKhiGiam));
        } else {
            responseDTO.setPriceAfterDiscount(String.valueOf(cartDetail.getSanPhamChiTiet().getGiaBan()));
        }
        BigDecimal tongTien = BigDecimal.valueOf(cartDetail.getSoLuong()).multiply(giaSauKhiGiam);
        responseDTO.setTotalPrice(String.valueOf(tongTien));

        return responseDTO;
    }
}
