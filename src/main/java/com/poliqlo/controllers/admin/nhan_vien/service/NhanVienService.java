package com.poliqlo.controllers.admin.nhan_vien.service;

import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.NhanVien;
import com.poliqlo.models.TaiKhoan;
import com.poliqlo.repositories.NhanVienRepository;
import com.poliqlo.repositories.TaiKhoanRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepository;
    @Autowired
    private BlobStoreService blobStoreService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    public void saveNhanVien(NhanVien nhanVien) {
        nhanVienRepository.save(nhanVien);
    }

    public List<NhanVien> getAllNhanVien() {
        return nhanVienRepository.findAll();
    }
    public NhanVien findById(Integer id) {
        return nhanVienRepository.findById(id).orElse(null);
    }
    public void deleteNhanVien(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        nhanVien.setIsDeleted(true);
        nhanVienRepository.save(nhanVien);

        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        taiKhoan.setIsDeleted(true);
        taiKhoanRepository.save(taiKhoan);
    }

    public Page<NhanVien> getAllNhanVienNotDeleted(Pageable pageable) {
        return nhanVienRepository.findAllByIsDeletedFalse(pageable);
    }

    public Page<NhanVien> getAllNhanVienDeleted(Pageable pageable) {
        return nhanVienRepository.findByIsDeletedTrue(pageable);
    }

    @Transactional
    public void restoreNhanVien(Integer nhanVienId) {
        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        if (nhanVien.getIsDeleted()) {
            nhanVien.setIsDeleted(false);
            nhanVienRepository.save(nhanVien);
        }
        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        if (taiKhoan.getIsDeleted()) {
            taiKhoan.setIsDeleted(false);
            taiKhoanRepository.save(taiKhoan);
        }
    }
    public void updateNhanVien(Integer id, NhanVien updatedNhanVien, MultipartFile file) throws IOException, java.io.IOException {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        TaiKhoan taiKhoan = nhanVien.getTaiKhoan();
        taiKhoan.setEmail(updatedNhanVien.getTaiKhoan().getEmail());
        taiKhoan.setSoDienThoai(updatedNhanVien.getTaiKhoan().getSoDienThoai());
        taiKhoan.setRole(updatedNhanVien.getTaiKhoan().getRole());

        if (file != null && !file.isEmpty()) {
            var blobResponse = blobStoreService.upload(file);
            taiKhoan.setAnhUrl(blobResponse.getUrl());
        }

        taiKhoanService.saveTaiKhoan(taiKhoan);
        nhanVien.setTen(updatedNhanVien.getTen());
        nhanVienRepository.save(nhanVien);
    }

}
