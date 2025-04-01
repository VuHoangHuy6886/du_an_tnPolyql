package com.poliqlo.repositories;

import com.poliqlo.models.HoaDon;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    Page<HoaDon> findByDateRangePaged(@Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate, Pageable pageable);

    @Query("SELECT hd FROM HoaDon hd WHERE " +
            "(:id IS NULL OR hd.id = :id) AND " +
            "(:trangThai IS NULL OR hd.trangThai = :trangThai) AND " +
            "(:loaiHoaDon IS NULL OR hd.loaiHoaDon = :loaiHoaDon) AND " +
            "(:minAmount IS NULL OR hd.tongTien >= :minAmount) AND " +
            "(:maxAmount IS NULL OR hd.tongTien <= :maxAmount) AND " +
            "(:fromDate IS NULL OR hd.ngayNhanHang >= :fromDate) AND " +
            "(:toDate IS NULL OR hd.ngayNhanHang <= :toDate) AND " +
            "hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> searchOrdersPaged(
            @Param("id") Integer id,
            @Param("trangThai") String trangThai,
            @Param("loaiHoaDon") String loaiHoaDon,
            @Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);

    // Phương thức để lấy đơn hàng trong khoảng ngày gần đây
    @Query("SELECT hd FROM HoaDon hd WHERE hd.ngayNhanHang >= :fromDate AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findRecentOrdersPaged(@Param("fromDate") LocalDateTime fromDate, Pageable pageable);

    long countAllByTrangThaiIn(List<String> trangThai);

    @Query("SELECT SUM(hd.tongTien) FROM HoaDon hd where hd.ngayTao> CURRENT_DATE AND (hd.isDeleted = false or hd.isDeleted=null) AND (hd.trangThai != 'DA_HUY'  or hd.trangThai != 'GIAO_HANG_THAT_BAI' or hd.trangThai != 'CHO_CHUYEN_HOAN') ")
    Optional<Long> getDaylyRevenue();

    @Query("SELECT SUM(hd.tongTien) FROM HoaDon hd " +
            "WHERE hd.ngayTao >= FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m-01') AND (hd.isDeleted = false or hd.isDeleted=null) AND (hd.trangThai != 'DA_HUY'  or hd.trangThai != 'GIAO_HANG_THAT_BAI' or hd.trangThai != 'CHO_CHUYEN_HOAN')")
    Optional<Long> getMonthlyRevenue();

    @Query(value = "SELECT COALESCE(SUM(tong_tien), 0) FROM hoa_don hd " +
            "WHERE NGAY_TAO >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-01') " +
            "AND NGAY_TAO < DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m-%d') AND hd.IS_DELETED = false AND (hd.TRANG_THAI != 'DA_HUY'  or hd.TRANG_THAI != 'GIAO_HANG_THAT_BAI' or hd.TRANG_THAI != 'CHO_CHUYEN_HOAN')",
            nativeQuery = true)
    Optional<Long> getSamePeriodLastMonthRevenue();



    @Query("SELECT hd FROM HoaDon hd " +
            "WHERE hd.ngayTao >= :fromDate and hd.ngayTao<=:toDate")
    List<HoaDon> findAllByNgayTaoBetween(LocalDateTime fromDate, LocalDateTime toDate);

    @Query("select concat(hdct.sanPhamChiTiet.sanPham.ten,' ',hdct.sanPhamChiTiet.mauSac.ten) as ten,sum(hdct.soLuong) as total_quantity, avg(hdct.giaKhuyenMai)  from HoaDonChiTiet hdct where " +
            "hdct.hoaDon.ngayTao<=:toDate and hdct.hoaDon.ngayTao>=:fromDate " +
            "AND (hdct.hoaDon.isDeleted = false or hdct.hoaDon.isDeleted=null) " +
            "AND (hdct.hoaDon.trangThai != 'DA_HUY'  or hdct.hoaDon.trangThai != 'GIAO_HANG_THAT_BAI' or hdct.hoaDon.trangThai != 'CHO_CHUYEN_HOAN') " +
            "group by ten " +
            "order by total_quantity desc ")
    List<Object[]> getTop10SanPhamBanChayNhat(LocalDateTime fromDate, LocalDateTime toDate, Limit limit);

    @Query("select " +
            "hdct.sanPhamChiTiet.sanPham.ten as ten," +
            "sum(hdct.soLuong*hdct.giaKhuyenMai) as doanhThu," +
            "sum(hdct.soLuong) as soLuongBan, " +
            "count(case WHEN hdct.hoaDon.loaiHoaDon ='TAI_QUAY' THEN 1 END) as soDonTaiQuay, " +
            "count(case WHEN hdct.hoaDon.loaiHoaDon !='TAI_QUAY' THEN 1 END) as soDonOnline " +
            "from HoaDonChiTiet hdct where " +
            "hdct.hoaDon.ngayTao<=:toDate and hdct.hoaDon.ngayTao>=:fromDate " +
            "AND (hdct.hoaDon.isDeleted = false or hdct.hoaDon.isDeleted=null) " +
            "AND (hdct.hoaDon.trangThai != 'DA_HUY'  or hdct.hoaDon.trangThai != 'GIAO_HANG_THAT_BAI' or hdct.hoaDon.trangThai != 'CHO_CHUYEN_HOAN') " +
            "group by ten ")
//            "order by :orderBy desc ")
    List<Object[]> getTop10SanPham(LocalDateTime fromDate, LocalDateTime toDate, Sort orderBy, Limit limit);


    @Query("SELECT hd FROM HoaDon hd WHERE hd.loaiHoaDon = :loaiHoaDon AND hd.isDeleted = false ORDER BY hd.id DESC")
    List<HoaDon> findAllByLoaiHoaDon(@Param("loaiHoaDon") String loaiHoaDon);

    @Query("SELECT hd FROM HoaDon hd WHERE hd.loaiHoaDon = :loaiHoaDon AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findAllByLoaiHoaDonPaged(@Param("loaiHoaDon") String loaiHoaDon, Pageable pageable);
    // Thêm phương thức mới để lấy đơn hàng theo nhiều trạng thái
    @Query("SELECT hd FROM HoaDon hd WHERE hd.trangThai IN :statuses AND hd.isDeleted = false ORDER BY hd.id DESC")
    Page<HoaDon> findAllByTrangThaiInPaged(@Param("statuses") List<String> statuses, Pageable pageable);
}