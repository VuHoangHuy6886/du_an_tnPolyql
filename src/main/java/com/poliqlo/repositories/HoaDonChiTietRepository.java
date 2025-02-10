package com.poliqlo.repositories;

import com.poliqlo.models.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer>, JpaSpecificationExecutor<HoaDonChiTiet> {
}