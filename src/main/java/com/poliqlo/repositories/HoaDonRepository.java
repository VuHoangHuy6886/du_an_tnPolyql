package com.poliqlo.repositories;

import com.poliqlo.models.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer>, JpaSpecificationExecutor<HoaDon> {
     final String CHO_XAC_NHAN="CHO_XAC_NHAN";
     final String XAC_NHAN="XAC_NHAN";
     final String DANG_CHUAN_BI_HANG="DANG_CHUAN_BI_HANG";
     final String CHO_LAY_HANG="CHO_LAY_HANG";
     final String LAY_HANG_THANH_CONG="LAY_HANG_THANH_CONG";
     final String DANG_VAN_CHUYEN="DANG_VAN_CHUYEN";
     final String DANG_GIAO="DANG_GIAO";
     final String GIAO_HANG_THANH_CONG="GIAO_HANG_THANH_CONG";
     final String GIAO_HANG_THAT_BAI="GIAO_HANG_THAT_BAI";
     final String CHO_CHUYEN_HOAN="CHO_CHUYEN_HOAN";
     final String THANH_CONG="THANH_CONG";
     final String DA_HUY="DA_HUY";
     final String THAT_LAC="THAT_LAC";

    // Lấy tất cả đơn hàng chưa bị xóa
    @Query("SELECT hd FROM HoaDon hd WHERE hd.isDeleted = false ORDER BY hd.id DESC")
    List<HoaDon> findAllActiveOrders();

    // Thêm phương thức mới hỗ trợ phân trang
    @Query("SELECT hd FROM HoaDon hd WHERE hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findAllActiveOrdersPaged(Pageable pageable);

    // Lấy đơn hàng theo ID khách hàng
    @Query("SELECT hd FROM HoaDon hd WHERE hd.khachHang.id = :khachHangId AND hd.isDeleted = false ORDER BY hd.id DESC")
    List<HoaDon> findAllByKhachHangId(@Param("khachHangId") Integer khachHangId);

    // Thêm phương thức mới hỗ trợ phân trang
    @Query("SELECT hd FROM HoaDon hd WHERE hd.khachHang.id = :khachHangId AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findAllByKhachHangIdPaged(@Param("khachHangId") Integer khachHangId, Pageable pageable);

    // Tìm đơn hàng theo ID
    @Query("SELECT hd FROM HoaDon hd WHERE hd.id = :hoaDonId AND hd.isDeleted = false")
    Optional<HoaDon> findActiveById(@Param("hoaDonId") Integer hoaDonId);

    // Lấy đơn hàng theo trạng thái
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai = :trangThai AND hd.isDeleted = false ORDER BY hd.id DESC")
    List<HoaDon> findAllByTrangThai(@Param("trangThai") String trangThai);

    // Thêm phương thức mới hỗ trợ phân trang
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai = :trangThai AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findAllByTrangThaiPaged(@Param("trangThai") String trangThai, Pageable pageable);

    // Tìm theo KH và trạng thái
    @Query("SELECT hd FROM HoaDon hd WHERE hd.khachHang.id = :khachHangId AND hd.trangThai = :trangThai AND hd.isDeleted = false ORDER BY hd.id DESC")
    List<HoaDon> findAllByKhachHangIdAndTrangThai(@Param("khachHangId") Integer khachHangId, @Param("trangThai") String trangThai);

    // Thêm phương thức mới hỗ trợ phân trang
    @Query("SELECT hd FROM HoaDon hd WHERE hd.khachHang.id = :khachHangId AND hd.trangThai = :trangThai AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findAllByKhachHangIdAndTrangThaiPaged(@Param("khachHangId") Integer khachHangId, @Param("trangThai") String trangThai, Pageable pageable);
    // Tìm kiếm đơn hàng theo mã
    @Query("SELECT hd FROM HoaDon hd WHERE hd.id = :id AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findByIdPaged(@Param("id") Integer id, Pageable pageable);

    // Tìm kiếm đơn hàng trong khoảng tiền
    @Query("SELECT hd FROM HoaDon hd WHERE hd.tongTien >= :minAmount AND hd.tongTien <= :maxAmount AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findByPriceRangePaged(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount, Pageable pageable);

    // Tìm kiếm đơn hàng trong khoảng ngày
    @Query("SELECT hd FROM HoaDon hd WHERE hd.ngayNhanHang >= :fromDate AND hd.ngayNhanHang <= :toDate AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findByDateRangePaged(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);

    // Tìm kiếm kết hợp: mã hóa đơn, khoảng tiền, khoảng thời gian, trạng thái
    @Query("SELECT hd FROM HoaDon hd WHERE " +
            "(:id IS NULL OR hd.id = :id) AND " +
            "(:trangThai IS NULL OR hd.trangThai = :trangThai) AND " +
            "(:minAmount IS NULL OR hd.tongTien >= :minAmount) AND " +
            "(:maxAmount IS NULL OR hd.tongTien <= :maxAmount) AND " +
            "(:fromDate IS NULL OR hd.ngayNhanHang >= :fromDate) AND " +
            "(:toDate IS NULL OR hd.ngayNhanHang <= :toDate) AND " +
            "hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> searchOrdersPaged(
            @Param("id") Integer id,
            @Param("trangThai") String trangThai,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);

    // Phương thức để lấy đơn hàng trong khoảng ngày gần đây
    @Query("SELECT hd FROM HoaDon hd WHERE hd.ngayNhanHang >= :fromDate AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findRecentOrdersPaged(@Param("fromDate") LocalDate fromDate, Pageable pageable);
}