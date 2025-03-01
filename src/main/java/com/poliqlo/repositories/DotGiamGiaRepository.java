package com.poliqlo.repositories;

import com.poliqlo.models.DotGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Query("SELECT d FROM DotGiamGia d WHERE "
            + "(:tenOrMa IS NULL OR (LOWER(d.ten) LIKE LOWER(CONCAT('%', :tenOrMa, '%')) OR LOWER(d.ma) LIKE LOWER(CONCAT('%', :tenOrMa, '%')))) "
            + "AND (:trangThai IS NULL OR d.trangThai = :trangThai) "
            + "AND (:thoiGianBatDau IS NULL OR d.thoiGianBatDau >= :thoiGianBatDau) "
            + "AND (:thoiGianKetThuc IS NULL OR d.thoiGianKetThuc <= :thoiGianKetThuc) "
            + "ORDER BY d.thoiGianBatDau DESC")
    Page<DotGiamGia> filterAllDiscount(Pageable pageable,
                                       @Param("tenOrMa") String tenOrMa,
                                       @Param("trangThai") String trangThai,
                                       @Param("thoiGianBatDau") LocalDateTime thoiGianBatDau,
                                       @Param("thoiGianKetThuc") LocalDateTime thoiGianKetThuc);


}