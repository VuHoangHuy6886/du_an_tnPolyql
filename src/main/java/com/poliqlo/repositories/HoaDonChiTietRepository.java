package com.poliqlo.repositories;

import com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.HoaDonChiTietDTO;
import com.poliqlo.models.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> , JpaSpecificationExecutor<HoaDonChiTiet> {
  @Query("SELECT new com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.HoaDonChiTietDTO(" +
          "h.id, " +
          "h.hoaDon.id, " +
          "h.sanPhamChiTiet.id, " +
          "h.sanPhamChiTiet.sanPham.ten, " +
          "h.giaGoc, " +
          "h.giaKhuyenMai, " +
          "h.soLuong) " +
          "FROM HoaDonChiTiet h " +
          "WHERE h.hoaDon.id = :hoaDonId ")
  List<HoaDonChiTietDTO> findByHoaDonId(@Param("hoaDonId") Integer hoaDonId);

}