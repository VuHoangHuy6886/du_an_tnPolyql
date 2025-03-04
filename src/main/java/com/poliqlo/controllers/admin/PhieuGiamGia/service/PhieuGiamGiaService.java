package com.poliqlo.controllers.admin.PhieuGiamGia.service;

import com.poliqlo.controllers.admin.PhieuGiamGia.model.request.AddPhieuGiamGiaRequest;
import com.poliqlo.controllers.admin.PhieuGiamGia.model.request.UpdatePhieuGiamGiaRequest;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.PhieuGiamGia;
import com.poliqlo.models.PhieuGiamGiaKhachHang;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.PhieuGiamGiaKhachHangRepository;
import com.poliqlo.repositories.PhieuGiamGiaRepository;
import com.poliqlo.utils.CouponStatusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PhieuGiamGiaService {

    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final KhachHangRepository khachHangRepository;
    private final PhieuGiamGiaKhachHangRepository phieuGiamGiaKhachHangRepository;

    public Page<PhieuGiamGia> findAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<PhieuGiamGia> phieuGiamGiaPage = phieuGiamGiaRepository.findAll(pageable);
        return phieuGiamGiaPage;
    }

    //ham loc
    public Page<PhieuGiamGia> findAll(int page, int size, String name, String status, LocalDateTime startTime, LocalDateTime endTime) {
        Pageable pageable = PageRequest.of(page, size);
        return phieuGiamGiaRepository.filterAllCoupon(pageable, name, status, startTime, endTime);
    }

    //ham them
    public String save(AddPhieuGiamGiaRequest request, List<Integer> ids) {
        System.out.println("request gia tri giam ; " + request.getGiaTriGiam());
        PhieuGiamGia add = new PhieuGiamGia();
        add.setMa(genMa());
        add.setTen(request.getTen());
        add.setSoLuong(Integer.parseInt(request.getSoLuong()));
        add.setHoaDonToiThieu(convertToBigDecimal(request.getHoaDonToiThieu()));
        BigDecimal giaTriDml = new BigDecimal(request.getGiaTriGiam());
        add.setGiaTriGiam(giaTriDml);
        add.setGiamToiDa(convertToBigDecimal(request.getGiamToiDa()));
        add.setLoaiHinhGiam(request.getLoaiHinhGiam());
        add.setTrangThai(request.getTrangThai());
        add.setNgayBatDau(request.getNgayBatDau());
        add.setNgayKetThuc(request.getNgayKetThuc());
        add.setTrangThai(CouponStatusUtil.getStatus(add.getNgayBatDau(), add.getNgayKetThuc())); //dam bao rang tham so truyen dung
        add.setIsDeleted(false);
        phieuGiamGiaRepository.save(add);
        this.savePhieuGiamChoKhachHang(ids, add);
        return "Thêm thành công";

    }

    //ham sua
    public String update(UpdatePhieuGiamGiaRequest request) {
        PhieuGiamGia add = new PhieuGiamGia();
        add.setId(request.getId());
        System.out.printf("id: " + add.getId());
        add.setMa(request.getMa());
        add.setTen(request.getTen());
        add.setSoLuong(Integer.parseInt(request.getSoLuong()));
        add.setHoaDonToiThieu(convertToBigDecimal(request.getHoaDonToiThieu()));
        BigDecimal giaTriDml = new BigDecimal(request.getGiaTriGiam());
        add.setGiaTriGiam(giaTriDml);
        add.setGiamToiDa(convertToBigDecimal(request.getGiamToiDa()));
        add.setLoaiHinhGiam(request.getLoaiHinhGiam());
        add.setTrangThai(request.getTrangThai());
        add.setNgayBatDau(request.getNgayBatDau());
        add.setNgayKetThuc(request.getNgayKetThuc());
        add.setIsDeleted(false);
        add.setTrangThai(CouponStatusUtil.getStatus(add.getNgayBatDau(), add.getNgayKetThuc())); //dam bao rang tham so truyen dung
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.saveAndFlush(add);
        return "Sửa thành công";
    }

    //tim kiem id
    public PhieuGiamGia findById(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id).get();
        return phieuGiamGia;
        //return  phieuGiamGiaRepository.findById(id).get();
    }

    // Hàm hỗ trợ: Chuyển đổi từ String sang BigDecimal (xử lý giá trị null và lỗi)
    private BigDecimal convertToBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị số không hợp lệ: " + value);
        }
    }

    // Hàm hỗ trợ: Chuyển đổi từ LocalDateTime sang Instant
    //gen ma tu repo
    public String genMa() {
        return phieuGiamGiaRepository.generateMaPhieuGiamGia();
    }

    public List<PhieuGiamGia> findAllList() {
        return phieuGiamGiaRepository.findAll();
    }

    public void updateStatusCoupon(List<PhieuGiamGia> phieuGiamGiaList
    ) {
        System.out.println("abc");
        LocalDateTime timeNow = LocalDateTime.now();
        // Định dạng thời gian để hiển thị
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formattedTime = timeNow.format(formatter);
        // Duyệt qua từng đợt giảm giá
        for (PhieuGiamGia pgg : phieuGiamGiaList) {
            LocalDateTime startTime = pgg.getNgayBatDau();
            LocalDateTime endTime = pgg.getNgayKetThuc();
            Integer sl = pgg.getSoLuong();

            //xet so luong
            if (sl <= 0) {
                pgg.setTrangThai(CouponStatusUtil.DA_KET_THUC);
                phieuGiamGiaRepository.save(pgg);
            }

            //xet trang thai
            if (timeNow.isBefore(startTime) && sl >= 1) {
                pgg.setTrangThai(CouponStatusUtil.SAP_DIEN_RA);
                phieuGiamGiaRepository.save(pgg);
            } else if (timeNow.isAfter(endTime)) {
                pgg.setTrangThai(CouponStatusUtil.DA_KET_THUC);
                phieuGiamGiaRepository.save(pgg);
            } else if (sl >= 1) {
                pgg.setTrangThai(CouponStatusUtil.DANG_DIEN_RA);
                phieuGiamGiaRepository.save(pgg);
            }

        }


    }

    public List<KhachHang> getAllCustomers() {
        return phieuGiamGiaRepository.findAllCustomers();
    }

    public void savePhieuGiamChoKhachHang(List<Integer> ids, PhieuGiamGia phieuGiamGia) {
        for (Integer id : ids) {
            KhachHang kh = khachHangRepository.findById(id).get();
            PhieuGiamGiaKhachHang pggKH = new PhieuGiamGiaKhachHang();
            pggKH.setPhieuGiamGia(phieuGiamGia);
            pggKH.setKhachHang(kh);
            pggKH.setIsUsed(false);
            phieuGiamGiaKhachHangRepository.save(pggKH);
        }
    }
///END
}
