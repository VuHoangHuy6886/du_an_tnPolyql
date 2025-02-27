package com.poliqlo.repositories;

import com.poliqlo.controllers.admin.san_pham.model.response.Response;
import com.poliqlo.models.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    List<SanPham> findAllByIsDeletedIsFalseOrderByIdDesc();

    @Query(value = """
        SELECT new com.poliqlo.controllers.admin.san_pham.model.response.Response(
            sp.id, sp.maSanPham, sp.ten, th.ten, cl.ten,kd.ten, dm.ten, sp.anhUrl,
            CAST(COALESCE(SUM(spct.soLuong), 0) AS integer),
            sp.trangThai
        )
        FROM SanPham sp
        LEFT JOIN ThuongHieu th ON th.id=sp.id
        LEFT JOIN ChatLieu cl ON cl.id=sp.id
        LEFT JOIN KieuDang kd ON kd.id=sp.id
        LEFT JOIN SanPhamDanhMuc spdm ON spdm.idSanPham.id = sp.id
        LEFT JOIN DanhMuc dm ON spdm.idSanPham.id = dm.id
        LEFT JOIN SanPhamChiTiet spct ON spct.idSanPham.id = sp.id
        WHERE sp.isDeleted = false
        GROUP BY sp.id, sp.maSanPham, sp.ten, th.ten, dm.ten, sp.anhUrl, sp.trangThai
    """)
    List<Response> findAllSanPham();
//    CAST(COALESCE(MAX(spct.giaBan), 0) AS double)

//    @Query("SELECT new com.poliqlo.controllers.admin.chi_tiet_san_pham.model.response.Response(sp.ten, spct.idKichThuoc, spct.giaBan, spct.soLuong, ha.duongDan, ms.tenMau) " +
//            "FROM SanPham sp " +
//            "JOIN SanPhamChiTiet spct ON sp.maSanPham = spct.idSanPham " +
//            "JOIN MauSac ms ON spct.idMauSac = ms.id " +
//            "LEFT JOIN HinhAnh ha ON ms.id = ha.maMau " +
//            "WHERE sp.maSP = :maSP")
//    List<com.poliqlo.controllers.admin.chi_tiet_san_pham.model.response.Response> findChiTietSanPhamById(@Param("maSP") Long maSP);

}
