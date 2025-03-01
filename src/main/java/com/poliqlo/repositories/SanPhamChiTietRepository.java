package com.poliqlo.repositories;

import com.poliqlo.models.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer>, JpaSpecificationExecutor<SanPhamChiTiet> {
  @Query("SELECT sp FROM SanPhamChiTiet sp WHERE sp.sanPham.id IN :ids")
  List<SanPhamChiTiet> findByListByIdProductsForDiscounts(@Param("ids") List<Integer> ids);
}