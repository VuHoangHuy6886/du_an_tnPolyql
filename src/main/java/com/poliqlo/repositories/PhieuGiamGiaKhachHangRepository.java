package com.poliqlo.repositories;

import com.poliqlo.models.PhieuGiamGiaKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhieuGiamGiaKhachHangRepository extends JpaRepository<PhieuGiamGiaKhachHang, Integer>, JpaSpecificationExecutor<PhieuGiamGiaKhachHang> {
}