package com.poliqlo.repositories;

import com.poliqlo.models.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer>, JpaSpecificationExecutor<LichSuHoaDon> {
    @Query("SELECT lshd FROM LichSuHoaDon lshd WHERE lshd.hoaDon.id = :hoaDonId AND lshd.isDeleted = false ORDER BY lshd.thoiGian DESC")
    List<LichSuHoaDon> findAllByHoaDonId(@Param("hoaDonId") Integer hoaDonId);

    List<LichSuHoaDon> findByHoaDonIdAndIsDeletedFalseOrderByThoiGianDesc(Integer hoaDonId);

    @Query("SELECT COALESCE(MAX(l.id), 0) FROM LichSuHoaDon l")
    int findMaxId();
}