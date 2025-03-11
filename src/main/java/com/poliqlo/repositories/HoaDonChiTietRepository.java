package com.poliqlo.repositories;

import com.poliqlo.models.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> , JpaSpecificationExecutor<HoaDonChiTiet> {

  @Query("SELECT hdct FROM HoaDonChiTiet hdct WHERE hdct.hoaDon.id = :hoaDonId AND hdct.isDeleted = false")
  List<HoaDonChiTiet> findAllByHoaDonId(@Param("hoaDonId") Integer hoaDonId);

  @Query("SELECT hdct FROM HoaDonChiTiet hdct JOIN FETCH hdct.sanPhamChiTiet spct JOIN FETCH spct.sanPham sp " +
          "JOIN FETCH spct.kichThuoc kt JOIN FETCH spct.mauSac ms " +
          "WHERE hdct.hoaDon.id = :hoaDonId AND hdct.isDeleted = false")
  List<HoaDonChiTiet> findAllDetailsByHoaDonId(@Param("hoaDonId") Integer hoaDonId);
  }