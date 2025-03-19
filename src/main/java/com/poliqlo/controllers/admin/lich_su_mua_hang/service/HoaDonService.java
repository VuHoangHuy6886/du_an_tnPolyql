package com.poliqlo.controllers.admin.lich_su_mua_hang.service;

import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.models.LichSuHoaDon;
import com.poliqlo.repositories.HoaDonChiTietRepository;
import com.poliqlo.repositories.HoaDonRepository;
import com.poliqlo.repositories.LichSuHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    // Thêm phương thức mới hỗ trợ phân trang
    public Page<HoaDon> getAllActiveOrdersPaged(Pageable pageable) {
        return hoaDonRepository.findAllActiveOrdersPaged(pageable);
    }

    public List<HoaDon> getOrdersByCustomerId(Integer khachHangId) {
        return hoaDonRepository.findAllByKhachHangId(khachHangId);
    }

    // Thêm phương thức mới hỗ trợ phân trang
    public Page<HoaDon> getOrdersByCustomerIdPaged(Integer khachHangId, Pageable pageable) {
        return hoaDonRepository.findAllByKhachHangIdPaged(khachHangId, pageable);
    }

    public List<HoaDon> getOrdersByStatus(String trangThai) {
        return hoaDonRepository.findAllByTrangThai(trangThai);
    }

    // Thêm phương thức mới hỗ trợ phân trang
    public Page<HoaDon> getOrdersByStatusPaged(String trangThai, Pageable pageable) {
        return hoaDonRepository.findAllByTrangThaiPaged(trangThai, pageable);
    }

    public List<HoaDon> getOrdersByCustomerIdAndStatus(Integer khachHangId, String trangThai) {
        return hoaDonRepository.findAllByKhachHangIdAndTrangThai(khachHangId, trangThai);
    }

    // Thêm phương thức mới hỗ trợ phân trang
    public Page<HoaDon> getOrdersByCustomerIdAndStatusPaged(Integer khachHangId, String trangThai, Pageable pageable) {
        return hoaDonRepository.findAllByKhachHangIdAndTrangThaiPaged(khachHangId, trangThai, pageable);
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

    public Page<HoaDon> searchByOrderId(Integer id, Pageable pageable) {
        return hoaDonRepository.findByIdPaged(id, pageable);
    }

    public Page<HoaDon> searchByPriceRange(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable) {
        return hoaDonRepository.findByPriceRangePaged(minAmount, maxAmount, pageable);
    }

    public Page<HoaDon> searchByDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return hoaDonRepository.findByDateRangePaged(fromDate, toDate, pageable);
    }

    public Page<HoaDon> searchOrders(
            Integer id,
            String trangThai,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable) {
        return hoaDonRepository.searchOrdersPaged(id, trangThai, minAmount, maxAmount, fromDate, toDate, pageable);
    }

    public Page<HoaDon> getRecentOrders(int days, Pageable pageable) {
        LocalDate fromDate = LocalDate.now().minusDays(days);
        return hoaDonRepository.findRecentOrdersPaged(fromDate, pageable);
    }


}