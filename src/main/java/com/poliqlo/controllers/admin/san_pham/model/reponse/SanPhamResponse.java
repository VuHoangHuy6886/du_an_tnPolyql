package com.poliqlo.controllers.admin.san_pham.model.reponse;

import lombok.Value;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */
@Value
public class SanPhamResponse implements Serializable {
    Integer id;
    Integer anhId;
    String anhUrl;
    String anhName;
    String tenSanPham;
    Integer binhLuanId;
    String binhLuanBinhLuanChiTiet;
    Integer manHinhCongNgheManHinhId;
    String manHinhDoPhanGiai;
    String manHinhManHinhRong;
    String manHinhDoSangToiDa;
    Integer manHinhMatKinhCamUngId;
    String cameraTruocDoPhanGiai;
    Set<Integer> cameraTruocTinhNangCameraIds;
    String cameraSauDoPhanGiai;
    String cameraSauDenFlash;
    Set<Integer> cameraSauTinhNangCameraIds;
    Integer heDieuHanhVaCpuHeDieuHanhId;
    Integer heDieuHanhVaCpuCpuId;
    String ketNoiMangDiDong;
    String ketNoiSim;
    Set<Integer> ketNoiWifiIds;
    Set<Integer> ketNoiGpIds;
    String ketNoiCongSac;
    String ketNoiJackTaiNghe;
    String ram;
    String thoiGianBaoHanh;
//    SanPhamRepository.TrangThai trangThai;
    Integer stt;
}