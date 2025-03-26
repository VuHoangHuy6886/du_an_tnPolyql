package com.poliqlo.repositories;

import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.SanPham;
import com.poliqlo.models.SanPhamChiTiet;
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

    @Query(value = "SELECT CONCAT('DGG', LPAD(COALESCE(MAX(CAST(SUBSTRING(ma, 4) AS UNSIGNED)), 0) + 1, 3, '0')) FROM dot_giam_gia",
            nativeQuery = true)
    String findNextMa();

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DotGiamGia d WHERE d.ten = :ten AND d.id <> :id")
    boolean existsByTenAndNotId(@Param("ten") String ten, @Param("id") Integer id);
    
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

    // query sản phẩm
    @Query(value = "SELECT * FROM san_pham sp WHERE (:name IS NULL OR sp.TEN LIKE %:name%) AND sp.IS_DELETED = 0",
            countQuery = "SELECT COUNT(*) FROM san_pham sp WHERE (:name IS NULL OR sp.TEN LIKE %:name%) AND sp.IS_DELETED = 0",
            nativeQuery = true)
    Page<SanPham> findSanPhamByName(@Param("name") String name, Pageable pageable);

    // query sản phẩm chi tiết theo id đợt giảm giá
    @Query(value = "SELECT ct.* " +
            "FROM san_pham_chi_tiet ct " +
            "INNER JOIN san_pham_chi_tiet_dot_giam_gia ctdgg ON ct.ID = ctdgg.san_pham_chi_tiet_id " +
            "INNER JOIN dot_giam_gia dgg ON dgg.id = ctdgg.dot_giam_gia_id " +
            "WHERE ctdgg.dot_giam_gia_id = :dotGiamGiaId AND dgg.IS_DELETED = false",
            nativeQuery = true)
    List<SanPhamChiTiet> findByDotGiamGiaId(@Param("dotGiamGiaId") Integer dotGiamGiaId);


}

