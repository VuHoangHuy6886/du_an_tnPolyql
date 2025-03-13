package com.poliqlo.repositories;

import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer>, JpaSpecificationExecutor<GioHangChiTiet> {
    @Query(value = "select gh from GioHangChiTiet gh where gh.khachHang.id = :idKH and gh.isDeleted = false ")
    List<GioHangChiTiet> gioHangChiTietFindByIdKH(@Param("idKH") Integer idKH);

    @Query(value = """
                SELECT dgg.* 
                FROM datn.dot_giam_gia AS dgg
                INNER JOIN datn.san_pham_chi_tiet_dot_giam_gia AS tg 
                    ON dgg.ID = tg.dot_giam_gia_id
                INNER JOIN datn.san_pham_chi_tiet AS spct 
                    ON spct.ID = tg.san_pham_chi_tiet_id
                WHERE spct.ID = :sanPhamChiTietId
                AND dgg.TRANG_THAI = 'DANG_DIEN_RA'
                ORDER BY dgg.THOI_GIAN_BAT_DAU DESC
                LIMIT 1
            """, nativeQuery = true)
    Optional<DotGiamGia> findLatestDiscountForProduct(@Param("sanPhamChiTietId") Integer sanPhamChiTietId);

    @Query(value = "SELECT gh FROM GioHangChiTiet gh WHERE gh.id IN :gioHangIds AND gh.isDeleted = false")
    List<GioHangChiTiet> findByListGioHangIds(@Param("gioHangIds") List<Integer> gioHangIds);

}