package com.poliqlo.repositories;

import com.poliqlo.models.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
}