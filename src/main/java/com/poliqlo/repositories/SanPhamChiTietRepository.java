package com.poliqlo.repositories;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response.ProductDetailDTO;
import com.poliqlo.models.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Integer> {
//    @Query(value = """
//    SELECT
//        spct.ID as id,
//        kt.TEN as kichThuoc,
//        CAST(spct.GIA_BAN AS double) as giaBan, -- ép kiểu tại đây
//        spct.SO_LUONG as soLuong,
//        ms.TEN as tenMau,
//        a.URL as anhUrlDetail
//    FROM SAN_PHAM_CHI_TIET spct
//    LEFT JOIN KICH_THUOC kt ON spct.ID_KICH_THUOC = kt.ID
//    LEFT JOIN SAN_PHAM sp ON sp.ID = spct.ID_SAN_PHAM
//    LEFT JOIN MAU_SAC ms ON spct.ID_MAU_SAC = ms.ID
//    LEFT JOIN ANH a ON spct.ID_SAN_PHAM = a.ID_SAN_PHAM AND ms.ID = a.ID_MAU_SAC
//    WHERE spct.IS_DELETED = b'0'
//      AND spct.ID_SAN_PHAM = :sanPhamId
//""", nativeQuery = true)
//    List<ProductDetailDTO> findProductDetailsBySanPhamId(@Param("sanPhamId") Integer sanPhamId);
    @Query(value = """
    SELECT 
        spct.ID AS id,
        sp.TEN AS tenSanPham,
        kt.TEN AS kichThuoc,
        CAST(spct.GIA_BAN AS double) as giaBan,
        spct.SO_LUONG AS soLuong,
        ms.TEN AS tenMau,
        a.URL AS anhUrlDetail
    FROM SAN_PHAM_CHI_TIET spct
    LEFT JOIN SAN_PHAM sp ON spct.ID_SAN_PHAM = sp.ID
    LEFT JOIN KICH_THUOC kt ON spct.ID_KICH_THUOC = kt.ID
    LEFT JOIN MAU_SAC ms ON spct.ID_MAU_SAC = ms.ID
    LEFT JOIN ANH a ON spct.ID_SAN_PHAM = a.ID_SAN_PHAM 
                    AND ms.ID = a.ID_MAU_SAC
    WHERE spct.IS_DELETED = 0
      AND spct.ID_SAN_PHAM = :sanPhamId
    GROUP BY spct.ID, sp.TEN, kt.TEN, spct.GIA_BAN, spct.SO_LUONG, ms.TEN, a.URL
""", nativeQuery = true)
    List<ProductDetailDTO> findProductDetailsBySanPhamId(@Param("sanPhamId") Integer sanPhamId);


}
