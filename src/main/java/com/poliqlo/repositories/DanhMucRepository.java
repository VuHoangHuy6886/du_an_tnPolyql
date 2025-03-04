package com.poliqlo.repositories;

import com.poliqlo.models.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> , JpaSpecificationExecutor<DanhMuc> {
  }