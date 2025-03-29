package com.poliqlo.repositories;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response.ProductDetailDTO;
import com.poliqlo.models.SanPhamChiTiet;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer>, JpaSpecificationExecutor<SanPhamChiTiet> {
  @Query("SELECT sp FROM SanPhamChiTiet sp WHERE sp.sanPham.id IN :ids")
  List<SanPhamChiTiet> findByListByIdProductsForDiscounts(@Param("ids") List<Integer> ids);
  boolean existsBySanPhamIdAndKichThuocIdAndMauSacId(Integer sanPhamId, Integer kichThuocId, Integer mauSacId);

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
    LEFT JOIN SAN_PHAM sp ON spct.SAN_PHAM_ID = sp.ID
    LEFT JOIN KICH_THUOC kt ON spct.KICH_THUOC_ID = kt.ID
    LEFT JOIN MAU_SAC ms ON spct.MAU_SAC_ID = ms.ID
    LEFT JOIN ANH a ON spct.SAN_PHAM_ID = a.SAN_PHAM_ID 
                    AND ms.ID = a.MAU_SAC_ID
    WHERE spct.IS_DELETED = 0
      AND spct.SAN_PHAM_ID = :sanPhamId
    GROUP BY spct.ID, sp.TEN, kt.TEN, spct.GIA_BAN, spct.SO_LUONG, ms.TEN, a.URL
""", nativeQuery = true)
    List<ProductDetailDTO> findProductDetailsBySanPhamId(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT spct.id FROM SanPhamChiTiet spct WHERE spct.barcode = :barcode")
    Optional<Integer> findFirstByBarcode(@Size(max = 100) String barcode, Limit limit);
}