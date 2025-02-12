package com.poliqlo.repositories;

import com.poliqlo.models.KieuDang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KieuDangRepository extends JpaRepository<KieuDang, Integer>, JpaSpecificationExecutor<KieuDang> {
}