package com.poliqlo.repositories;

import com.poliqlo.models.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> , JpaSpecificationExecutor<ThongBao> {
  }