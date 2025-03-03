package com.poliqlo.repositories;

import com.poliqlo.models.SanPhamDanhMuc;
import com.poliqlo.models.SanPhamDanhMucId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SanPhamDanhMucRepository extends JpaRepository<SanPhamDanhMuc, SanPhamDanhMucId> , JpaSpecificationExecutor<SanPhamDanhMuc> {
  }