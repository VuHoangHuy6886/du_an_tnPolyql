package com.poliqlo.repositories;

import com.poliqlo.models.KhachHang;
import com.poliqlo.models.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer>, JpaSpecificationExecutor<PhieuGiamGia> {

    //gen ma theo cu phap
    @Query(value = "SELECT COALESCE(MAX(ma), 'PGG000') FROM phieu_giam_gia", nativeQuery = true)
    String findMaxMa();

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM PhieuGiamGia d WHERE d.ten = :ten AND d.id <> :id")
    boolean existsByTenAndNotId(@Param("ten") String ten, @Param("id") Integer id);

    default String generateMaPhieuGiamGia() {
        String maxMa = findMaxMa(); // Gọi phương thức tìm mã lớn nhất hiện có
        int number = Integer.parseInt(maxMa.substring(3)); // Lấy phần số từ mã (bỏ "DGG")
        return String.format("PGG%03d", number + 1); // Tạo mã mới dạng DGGxxx với số tăng lên 1
    }

    @Query(value = "SELECT a FROM PhieuGiamGia a WHERE "
            + "(:name IS NULL OR a.ten like %:name% or :name is null or a.ma like %:name%) "
            + "AND (:trangThai IS NULL OR a.trangThai = :trangThai) "
            + "AND (:ngayBatDau IS NULL OR a.ngayBatDau >= :ngayBatDau) "
            + "AND (:ngayKetThuc IS NULL OR a.ngayKetThuc <= :ngayKetThuc) "
            + "AND a.isDeleted = false "
            + "ORDER BY a.ngayBatDau ASC")
    Page<PhieuGiamGia> filterAllCoupon(Pageable pageable,
                                       @Param("name") String name,
                                       @Param("trangThai") String trangThai,
                                       @Param("ngayBatDau") LocalDateTime ngayBatDau,
                                       @Param("ngayKetThuc") LocalDateTime ngayKetThuc);

    @Query(value = "SELECT * FROM khach_hang", nativeQuery = true)
    List<KhachHang> findAllCustomers();

    @Query(value = "SELECT p FROM PhieuGiamGia p " +
            "INNER JOIN PhieuGiamGiaKhachHang pg ON pg.phieuGiamGia.id = p.id " +
            "INNER JOIN KhachHang kh ON kh.id = pg.khachHang.id " +
            "WHERE p.trangThai = 'DANG_DIEN_RA' AND kh.id = :idKH AND p.hoaDonToiThieu <= :tongTien")
    List<PhieuGiamGia> hienThiPhieuGiamBangIdKhachHang(@Param("idKH") Integer idKH, @Param("tongTien") BigDecimal tongTien);
}