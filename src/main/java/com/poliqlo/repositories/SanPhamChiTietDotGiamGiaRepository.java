package com.poliqlo.repositories;

import com.poliqlo.models.SanPhamChiTietDotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SanPhamChiTietDotGiamGiaRepository extends JpaRepository<SanPhamChiTietDotGiamGia, Integer>, JpaSpecificationExecutor<SanPhamChiTietDotGiamGia> {
}