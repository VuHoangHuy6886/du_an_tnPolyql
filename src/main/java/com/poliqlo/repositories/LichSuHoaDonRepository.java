package com.poliqlo.repositories;

import com.poliqlo.models.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer>, JpaSpecificationExecutor<LichSuHoaDon> {
}