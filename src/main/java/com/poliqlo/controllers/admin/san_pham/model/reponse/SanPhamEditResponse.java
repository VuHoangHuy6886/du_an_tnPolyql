package com.poliqlo.controllers.admin.san_pham.model.reponse;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class SanPhamEditResponse implements Serializable {

    Integer id;
    String tenSanPham;
    String anhUrl;
    String anhName;
    String anhId;
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
    Set<SanPhamChiTietDto> sanPhamChiTiet;



    /**
     * DTO for {@link com.poly.polystore.entity.SanPhamChiTiet}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class SanPhamChiTietDto implements Serializable {
        Integer id;
        List<KhuyenMaiDto> khuyenMai;
        List<AnhDto> anh;
        MauSacDto mauSac;
        String rom;
        BigDecimal giaBan;
        BigDecimal giaNhap;
//        SanPhamRepository.TrangThai trangThai;
        Integer stt;

        /**
         * DTO for {@link com.poly.polystore.entity.KhuyenMai}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class KhuyenMaiDto implements Serializable {
            Integer id;
        }

        /**
         * DTO for {@link com.poly.polystore.entity.Anh}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class AnhDto implements Serializable {
            Integer id;
            String name;
            String url;
        }

        /**
         * DTO for {@link com.poly.polystore.entity.MauSac}
         */
        @AllArgsConstructor
        @Getter
        @Setter
        @NoArgsConstructor
        @Builder
        public static class MauSacDto implements Serializable {
            Integer id;
        }
    }
}