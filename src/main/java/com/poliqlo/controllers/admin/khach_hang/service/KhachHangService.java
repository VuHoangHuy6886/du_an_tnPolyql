package com.poliqlo.controllers.admin.khach_hang.service;

import com.poliqlo.controllers.admin.nhan_vien.service.TaiKhoanService;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.NhanVien;
import com.poliqlo.models.TaiKhoan;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.TaiKhoanRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class KhachHangService {
    @Autowired
    private BlobStoreService blobStoreService;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public void saveKhachHang(KhachHang khachHang) {
        khachHangRepository.save(khachHang);
    }
    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }
    public KhachHang findById(Integer id) {
        return khachHangRepository.findById(id).orElse(null);
    }
    public void deleteKhachHang(Integer id) {
        KhachHang khachHang = khachHangRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        khachHang.setIsDeleted(true);
        khachHangRepository.save(khachHang);

        TaiKhoan taiKhoan = khachHang.getTaiKhoan();
        taiKhoan.setIsDeleted(true);
        taiKhoanRepository.save(taiKhoan);
    }
    public Page<KhachHang> getAllKhachHangNotDeleted(Pageable pageable) {
        return khachHangRepository.findAllByIsDeletedFalse(pageable);
    }
    public void updateKhachHang(Integer id, KhachHang updatedKhachHang, MultipartFile file) throws IOException, java.io.IOException {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        TaiKhoan taiKhoan = khachHang.getTaiKhoan();
        taiKhoan.setEmail(updatedKhachHang.getTaiKhoan().getEmail());
        taiKhoan.setSoDienThoai(updatedKhachHang.getTaiKhoan().getSoDienThoai());

        if (file != null && !file.isEmpty()) {
            var blobResponse = blobStoreService.upload(file);
            taiKhoan.setAnhUrl(blobResponse.getUrl());
        }

        taiKhoanService.saveTaiKhoan(taiKhoan);
        khachHang.setTen(updatedKhachHang.getTen());
        khachHang.setNgaySinh(updatedKhachHang.getNgaySinh());
        khachHang.setGioiTinh(updatedKhachHang.getGioiTinh());
        khachHangRepository.save(khachHang);
    }
    public Page<KhachHang> getAllKhachHangDeleted(Pageable pageable) {
        return khachHangRepository.findByIsDeletedTrue(pageable);
    }
    @Transactional
    public void restoreKhachHang(Integer khachHangId) {
        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        if (khachHang.getIsDeleted()) {
            khachHang.setIsDeleted(false);
            khachHangRepository.save(khachHang);
        }
        TaiKhoan taiKhoan = khachHang.getTaiKhoan();
        if (taiKhoan.getIsDeleted()) {
            taiKhoan.setIsDeleted(false);
            taiKhoanRepository.save(taiKhoan);
        }
    }
}
