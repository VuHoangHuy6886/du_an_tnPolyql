package com.poliqlo.controllers.admin.san_pham.model.reponse;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class SanPhamTemplate implements Serializable {
    Integer id;
    String tenSanPham;
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
    Set<Integer> ketNoiGpsIds;
    Set<Integer> ketNoiBluetoothIds;
    String ketNoiCongSac;
    String ketNoiJackTaiNghe;
    String pinVaSacDungLuongPin;
    String pinVaSacLoaiPin;
    String pinVaSacHoTroSacToiDa;
    Set<Integer> pinVaSacCongNghePinIds;
    String thongTinChungThietKe;
    String thongTinChungChatLieu;
    String thongTinChungKichThuocKhoiLuong;
    List<Integer> khuyenMaiIds;
    Integer thuongHieuId;
    String ram;
    String thoiGianBaoHanh;
//    SanPhamRepository.TrangThai trangThai;
    Integer stt;
    Set<Integer> sanPhamChiTietMauSacIds;
    Set<Integer> thongTinChungTinhNangDacBietIds;
    Set<String> sanPhamChiTietRoms;





}