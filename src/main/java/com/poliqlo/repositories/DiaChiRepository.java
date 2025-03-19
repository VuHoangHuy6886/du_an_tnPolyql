package com.poliqlo.repositories;

import com.poliqlo.models.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaChiRepository extends JpaRepository<DiaChi, Integer>, JpaSpecificationExecutor<DiaChi> {

    @Query("select d from DiaChi d where d.khachHang.id = :id and d.isDeleted = false")
    List<DiaChi> timKiemDiaChiTheoIdKhachHang(@Param("id") Integer id);

    @Query("select d from DiaChi d where d.khachHang.id = :id and d.isDefault = true ")
    DiaChi findAddressDefault(@Param("id") Integer id);

    @Query("select d from DiaChi d where d.id = :id ")
    DiaChi findDiaChiById(@Param("id") Integer id);
}