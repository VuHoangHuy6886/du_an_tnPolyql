package com.poliqlo.repositories;

import com.poliqlo.models.KhachHang;
import com.poliqlo.models.PhieuGiamGia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer>, JpaSpecificationExecutor<PhieuGiamGia> {

    //gen ma theo cu phap
    @Query(value = "SELECT COALESCE(MAX(ma), 'PGG000') FROM phieu_giam_gia", nativeQuery = true)
    String findMaxMa();

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM PhieuGiamGia d WHERE d.ten = :ten AND (:id = 0 OR d.id <> :id)")
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

    @Query("SELECT k FROM KhachHang k WHERE LOWER(k.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    Page<KhachHang> searchByTen(@Param("ten") String ten, Pageable pageable);

    @Query(value = "SELECT * FROM khach_hang", nativeQuery = true)
    List<KhachHang> findAllCustomers();
    @Query("SELECT p FROM PhieuGiamGia p WHERE p.id = :id")
    Optional<PhieuGiamGia> findby(@Param("id") Long id);

    boolean existsByTen(@Size(max = 255) @NotNull String ten);

    boolean existsByGiaTriGiam(@NotNull BigDecimal giaTriGiam);
    @Query(value = "SELECT p FROM PhieuGiamGia p " +
            "INNER JOIN PhieuGiamGiaKhachHang pg ON pg.phieuGiamGia.id = p.id " +
            "INNER JOIN KhachHang kh ON kh.id = pg.khachHang.id " +
            "WHERE p.trangThai = 'DANG_DIEN_RA' AND kh.id = :idKH AND p.hoaDonToiThieu <= :tongTien")
    List<PhieuGiamGia> hienThiPhieuGiamBangIdKhachHang(@Param("idKH") Integer idKH, @Param("tongTien") BigDecimal tongTien);

    @Query("SELECT p FROM PhieuGiamGia p " +
            "LEFT JOIN PhieuGiamGiaKhachHang pg ON pg.phieuGiamGia.id = p.id " +
            "WHERE p.trangThai = 'DANG_DIEN_RA' " +
            "AND pg.id IS NULL " +
            "AND p.hoaDonToiThieu <= :tongTien")
    List<PhieuGiamGia> findCouponsNotApplied(@Param("tongTien") BigDecimal tongTien);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM PhieuGiamGia p " +
            "LEFT JOIN PhieuGiamGiaKhachHang pg ON pg.phieuGiamGia.id = p.id " +
            "WHERE p.trangThai = 'DANG_DIEN_RA' " +
            "AND pg.id IS NULL")
    Boolean existsCouponsNotApplied();
    @Query("SELECT pgg FROM PhieuGiamGia pgg " +
            "LEFT JOIN pgg.khachHangs kh " +
            "WHERE CURRENT_TIMESTAMP BETWEEN pgg.ngayBatDau AND pgg.ngayKetThuc " +
            "AND pgg.soLuong > 0 " +
            "AND  kh IS NULL " +
            "AND (pgg.hoaDonToiThieu<= :price or pgg.hoaDonToiThieu=null) " +
            "AND pgg.isDeleted = false "
    )
    Page<PhieuGiamGia> findAllActive(Double price,Pageable page);

    @Query("SELECT pgg FROM PhieuGiamGia pgg " +
            "LEFT JOIN pgg.khachHangs kh " +
            "WHERE CURRENT_TIMESTAMP BETWEEN pgg.ngayBatDau AND pgg.ngayKetThuc " +
            "AND pgg.soLuong > 0 " +
            "AND (kh.id = :khachHangId OR kh IS NULL )" +
            "AND (pgg.hoaDonToiThieu<= :price or pgg.hoaDonToiThieu=null) " +
            "AND pgg.isDeleted = false "
    )


    Page<PhieuGiamGia> findAllActive(Integer khachHangId, Double price, Pageable page);

}