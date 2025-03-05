package com.poliqlo.controllers.admin.nhan_vien.service;

import com.poliqlo.models.TaiKhoan;
import com.poliqlo.repositories.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    public void saveTaiKhoan(TaiKhoan taiKhoan) {
        taiKhoanRepository.save(taiKhoan);
    }
    public void deleteTaiKhoan(Integer id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        taiKhoan.setIsDeleted(true);
        taiKhoanRepository.save(taiKhoan);
    }
    public boolean existsByEmail(String email) {
        return taiKhoanRepository.existsByEmail(email);
    }

    public boolean existsBySoDienThoai(String soDienThoai) {
        return taiKhoanRepository.existsBySoDienThoai(soDienThoai);
    }


}
