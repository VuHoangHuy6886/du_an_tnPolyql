package com.poliqlo.controllers.admin.lich_su_mua_hang.service;

import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.models.LichSuHoaDon;
import com.poliqlo.repositories.HoaDonChiTietRepository;
import com.poliqlo.repositories.HoaDonRepository;
import com.poliqlo.repositories.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;

    @Autowired
    public HoaDonService(HoaDonRepository hoaDonRepository,
                         HoaDonChiTietRepository hoaDonChiTietRepository,
                         LichSuHoaDonRepository lichSuHoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
    }

    public List<HoaDon> getAllActiveOrders() {
        return hoaDonRepository.findAllActiveOrders();
    }

    public List<HoaDon> getOrdersByCustomerId(Integer khachHangId) {
        return hoaDonRepository.findAllByKhachHangId(khachHangId);
    }

    public List<HoaDon> getOrdersByStatus(String trangThai) {
        return hoaDonRepository.findAllByTrangThai(trangThai);
    }

    public List<HoaDon> getOrdersByCustomerIdAndStatus(Integer khachHangId, String trangThai) {
        return hoaDonRepository.findAllByKhachHangIdAndTrangThai(khachHangId, trangThai);
    }

    public Optional<HoaDon> getOrderById(Integer hoaDonId) {
        return hoaDonRepository.findActiveById(hoaDonId);
    }

    public List<HoaDonChiTiet> getOrderDetailsByOrderId(Integer hoaDonId) {
        return hoaDonChiTietRepository.findAllByHoaDonId(hoaDonId);
    }

    public List<HoaDonChiTiet> getOrderDetailsWithProducts(Integer hoaDonId) {
        return hoaDonChiTietRepository.findAllDetailsByHoaDonId(hoaDonId);
    }

    public List<LichSuHoaDon> getOrderHistoryByOrderId(Integer hoaDonId) {
        return lichSuHoaDonRepository.findAllByHoaDonId(hoaDonId);
    }
}
