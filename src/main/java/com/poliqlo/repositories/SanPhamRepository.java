package com.poliqlo.repositories;

import com.poliqlo.controllers.admin.san_pham.model.response.Response;
import com.poliqlo.models.SanPham;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    List<SanPham> findAllByIsDeletedIsFalseOrderByIdDesc();

    @Query(value = """
    SELECT 
        SP.ID, 
        SP.MA_SAN_PHAM, 
        SP.TEN, 
        TH.TEN AS THUONG_HIEU, 
        CL.TEN AS CHAT_LIEU, 
        KD.TEN AS KIEU_DANG, 
        GROUP_CONCAT(DISTINCT DM.TEN ORDER BY DM.TEN SEPARATOR ', ') AS DANH_MUC, 
        SP.ANH_URL, 
        T.SO_LUONG, 
        SP.TRANG_THAI
    FROM SAN_PHAM SP
    LEFT JOIN THUONG_HIEU TH ON TH.ID = SP.ID_THUONG_HIEU
    LEFT JOIN CHAT_LIEU CL ON CL.ID = SP.ID_CHAT_LIEU
    LEFT JOIN KIEU_DANG KD ON KD.ID = SP.ID_KIEU_DANG
    LEFT JOIN SAN_PHAM_DANH_MUC SPDM ON SPDM.ID_SAN_PHAM = SP.ID
    LEFT JOIN DANH_MUC DM ON DM.ID = SPDM.ID_DANH_MUC
    LEFT JOIN (
        SELECT SPCT.ID_SAN_PHAM, CAST(COALESCE(SUM(SPCT.SO_LUONG), 0) AS UNSIGNED) AS SO_LUONG
        FROM SAN_PHAM_CHI_TIET SPCT
        GROUP BY SPCT.ID_SAN_PHAM
    ) AS T ON T.ID_SAN_PHAM = SP.ID
    WHERE SP.IS_DELETED = FALSE
    GROUP BY SP.ID, SP.MA_SAN_PHAM, SP.TEN, TH.TEN, CL.TEN, KD.TEN, SP.ANH_URL, T.SO_LUONG, SP.TRANG_THAI;
    """, nativeQuery = true)
    List<Object[]> findAllSanPham();
//    CAST(COALESCE(MAX(spct.giaBan), 0) AS double)

    @Modifying
    @Transactional
    @Query("UPDATE SanPham sp SET sp.maSanPham = :maSanPham, sp.ten = :ten, " +
            "sp.idThuongHieu = (SELECT th FROM ThuongHieu th WHERE th.id = :idThuongHieu), " +
            "sp.idChatLieu = (SELECT cl FROM ChatLieu cl WHERE cl.id = :idChatLieu), " +
            "sp.idKieuDang = (SELECT kd FROM KieuDang kd WHERE kd.id = :idKieuDang) " +
            "WHERE sp.id = :id")
    int updateSanPham(
            @Param("id") Integer id,
            @Param("maSanPham") String maSanPham,
            @Param("ten") String ten,
            @Param("idThuongHieu") Integer idThuongHieu,
            @Param("idChatLieu") Integer idChatLieu,
            @Param("idKieuDang") Integer idKieuDang
    );


}
