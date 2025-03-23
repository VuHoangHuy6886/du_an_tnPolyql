package com.poliqlo.controllers.admin.xu_ly_don_hang.service;


import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.repositories.HoaDonChiTietRepository;
import com.poliqlo.repositories.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public Optional<HoaDon> getOrderById(Integer id) {
        return hoaDonRepository.findById(id);
    }
    public Optional<HoaDonChiTiet> getSPCTId(Integer id) {
        return hoaDonChiTietRepository.findById(id);
    }


    public HoaDon updateOrderStatus(Integer id, String newStatus) {
        Optional<HoaDon> orderOptional = hoaDonRepository.findById(id);
        if (orderOptional.isPresent()) {
            HoaDon order = orderOptional.get();
            order.setTrangThai(newStatus);
            return hoaDonRepository.save(order);
        }
        return null;
    }

    public HoaDonChiTiet updateOrderDetail(Integer orderId, Integer detailId, Integer soLuong) {
        Optional<HoaDonChiTiet> detailOptional = hoaDonChiTietRepository.findById(detailId);
        if (detailOptional.isPresent()) {
            HoaDonChiTiet detail = detailOptional.get();
            detail.setSoLuong(soLuong);
            return hoaDonChiTietRepository.save(detail);
        }
        return null;
    }

    // Soft delete: đánh dấu xóa (isDeleted = true)
    public boolean deleteOrderDetail(Integer orderId, Integer detailId) {
        Optional<HoaDonChiTiet> detailOptional = hoaDonChiTietRepository.findById(detailId);
        if(detailOptional.isPresent()){
            HoaDonChiTiet detail = detailOptional.get();
            detail.setIsDeleted(true);
            hoaDonChiTietRepository.save(detail);
            return true;
        }
        return false;
    }

    // Undo soft delete: khôi phục (isDeleted = false)
    public HoaDonChiTiet undoDeleteOrderDetail(Integer orderId, Integer detailId) {
        Optional<HoaDonChiTiet> detailOptional = hoaDonChiTietRepository.findById(detailId);
        if(detailOptional.isPresent()){
            HoaDonChiTiet detail = detailOptional.get();
            detail.setIsDeleted(false);
            return hoaDonChiTietRepository.save(detail);
        }
        return null;
    }

    // Final delete: xóa hoàn toàn
    public boolean finalDeleteOrderDetail(Integer orderId, Integer detailId) {
        Optional<HoaDonChiTiet> detailOptional = hoaDonChiTietRepository.findById(detailId);
        if(detailOptional.isPresent()){
            HoaDonChiTiet detail = detailOptional.get();
            detail.setIsDeleted(true);
            hoaDonChiTietRepository.save(detail);
            return true;
        }
        return false;
    }



}

