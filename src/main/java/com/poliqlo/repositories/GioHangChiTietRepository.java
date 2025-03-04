package com.poliqlo.repositories;

import com.poliqlo.models.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer> , JpaSpecificationExecutor<GioHangChiTiet> {
  }