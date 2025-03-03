package com.poliqlo.repositories;

import com.poliqlo.models.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> , JpaSpecificationExecutor<SanPhamChiTiet> {
  }