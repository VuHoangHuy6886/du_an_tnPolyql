package com.poliqlo.controllers.client.carts.mapper;

import com.poliqlo.controllers.client.carts.dto.BillRequestDTO;
import com.poliqlo.models.*;
import com.poliqlo.repositories.HoaDonRepository;
import com.poliqlo.utils.BillStatus;

import java.math.BigDecimal;

public class BillMapper {
    public static HoaDon BillRequestToBill(BillRequestDTO billRequestDTO, PhieuGiamGia phieuGiamGia, KhachHang khachHang, DiaChi diaChi) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setKhachHang(khachHang);
        if (phieuGiamGia != null) {
            hoaDon.setPhieuGiamGia(phieuGiamGia);
           // hoaDon.setGiamMaGiamGia(phieuGiamGia.getMa());
        }
        hoaDon.setGiamMaGiamGia(BigDecimal.valueOf(Long.parseLong(billRequestDTO.getCoupon())));
        hoaDon.setSoDienThoai(diaChi.getSoDienThoai());
        hoaDon.setDiaChi(diaChi.getAddress());
        hoaDon.setTenNguoiNhan(diaChi.getHoTenNguoiNhan());
        hoaDon.setPhiVanChuyen(BigDecimal.valueOf(Long.parseLong(billRequestDTO.getShipping())));
        hoaDon.setTongTien(BigDecimal.valueOf(Long.parseLong(billRequestDTO.getTotalPrice())));
        hoaDon.setPhuongThucThanhToan(billRequestDTO.getPaymentMethod());
        hoaDon.setGhiChu(billRequestDTO.getNote());
        hoaDon.setTrangThai(HoaDonRepository.CHO_XAC_NHAN);
        hoaDon.setLoaiHoaDon("ONLINE");
        hoaDon.setIsDeleted(false);
        return hoaDon;

    }
}
