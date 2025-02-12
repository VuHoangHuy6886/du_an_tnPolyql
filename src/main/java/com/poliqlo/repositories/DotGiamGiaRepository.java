package com.poliqlo.repositories;

import com.poliqlo.models.DotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> , JpaSpecificationExecutor<DotGiamGia> {
  }