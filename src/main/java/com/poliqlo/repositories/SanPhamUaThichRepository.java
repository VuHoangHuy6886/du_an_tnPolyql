package com.poliqlo.repositories;

import com.poliqlo.models.SanPhamUaThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SanPhamUaThichRepository extends JpaRepository<SanPhamUaThich, Integer>, JpaSpecificationExecutor<SanPhamUaThich> {
}