package com.poliqlo.repositories;

import com.poliqlo.models.DotGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer>, JpaSpecificationExecutor<DotGiamGia> {
    boolean existsByTen(String ten);

    @Query(value = "SELECT COALESCE(MAX(ma), 'DGG000') FROM dot_giam_gia", nativeQuery = true)
    String findMaxMa();

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DotGiamGia d WHERE d.ten = :ten AND d.id <> :id")
    boolean existsByTenAndNotId(@Param("ten") String ten, @Param("id") Integer id);

    default String generateMaDotGiamGia() {
        String maxMa = findMaxMa();
        int number = Integer.parseInt(maxMa.substring(3)); // Lấy phần số từ mã
        return String.format("DGG%03d", number + 1); // Tăng số và format lại thành dạng DGGxxx
    }

    @Query(value = "SELECT d FROM DotGiamGia d WHERE "
            + "(:name IS NULL OR d.ten like %:name% or :name is null or d.ma like %:name%) "
            + "AND (:trangThai IS NULL OR d.trangThai = :trangThai) "
            + "AND (:thoiGianBatDau IS NULL OR d.thoiGianBatDau >= :thoiGianBatDau) "
            + "AND (:thoiGianKetThuc IS NULL OR d.thoiGianKetThuc <= :thoiGianKetThuc) "
            + "AND d.isDeleted = false "
            + "ORDER BY d.thoiGianBatDau ASC")
    Page<DotGiamGia> filterAllDiscount(Pageable pageable,
                                       @Param("name") String name,
                                       @Param("trangThai") String trangThai,
                                       @Param("thoiGianBatDau") LocalDateTime thoiGianBatDau,
                                       @Param("thoiGianKetThuc") LocalDateTime thoiGianKetThuc);

    @Query(value = "SELECT d FROM DotGiamGia d WHERE "
            + "d.isDeleted = false "
            + "ORDER BY d.thoiGianBatDau ASC")
    Page<DotGiamGia> findAllByIsDeletedFalse(Pageable pageable);

    @Query(value = "SELECT d FROM DotGiamGia d WHERE "
            + "d.isDeleted = true "
            + "ORDER BY d.thoiGianBatDau ASC")
    List<DotGiamGia> findAllByIsDeletedTrue();
}

