package com.poliqlo.repositories;

import com.poliqlo.models.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> , JpaSpecificationExecutor<ThuongHieu> {
  }