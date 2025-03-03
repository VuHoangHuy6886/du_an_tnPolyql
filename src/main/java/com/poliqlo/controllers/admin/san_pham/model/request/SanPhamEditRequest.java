package com.poliqlo.controllers.admin.san_pham.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
//@NoArgsConstructor
public class SanPhamEditRequest implements Serializable {
//    Integer id;
//    AnhDto anh;
//    @NotBlank
//    String tenSanPham;
//    ManHinhDto manHinh;
//    FontCameraDto cameraTruoc;
//    BackCameraDto cameraSau;
//    HeDieuHanhVaCpuDto heDieuHanhVaCpu;
//    KetNoiDto ketNoi;
//    PinVaSacDto pinVaSac;
//    ThongTinChungDto thongTinChung;
//    List<KhuyenMaiDto> khuyenMai;
//    @NotNull
//    SanPhamEditRequest.SeriesDto thuongHieu;
//    @NotNull
//    Set<SanPhamChiTietDto> sanPhamChiTiet;
//    @NotNull
//    @NotBlank
//    String ram;
//    @NotNull
//    String thoiGianBaoHanh;
//    @NotNull
//    SanPhamRepository.TrangThai trangThai;
//    String moTa;
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.Anh}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class AnhDto implements Serializable {
//        Integer id;
//        String name;
//        String url;
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.ManHinh}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class ManHinhDto implements Serializable {
//        CongNgheManHinhDto congNgheManHinh;
//        @NotBlank
//        String doPhanGiai;
//        @NotBlank
//        String manHinhRong;
//        @NotBlank
//        String doSangToiDa;
//        MatKinhCamUngDto matKinhCamUng;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.CongNgheManHinh}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class CongNgheManHinhDto implements Serializable {
//            @NotNull
//            @Positive
//            Integer id;
//        }
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.MatKinhCamUng}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class MatKinhCamUngDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.FontCamera}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class FontCameraDto implements Serializable {
//        @NotNull
//        String doPhanGiai;
//        Set<TinhNangCameraDto> tinhNangCameras;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.TinhNangCamera}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class TinhNangCameraDto implements Serializable {
//            @NotNull
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.BackCamera}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class BackCameraDto implements Serializable {
//        @NotBlank
//        String doPhanGiai;
//        @NotBlank
//        String denFlash;
//        Set<TinhNangCameraDto> tinhNangCameras;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.TinhNangCamera}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class TinhNangCameraDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.HeDieuHanhVaCpu}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class HeDieuHanhVaCpuDto implements Serializable {
//        HeDieuHanhDto heDieuHanh;
//        CpuDto cpu;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.HeDieuHanh}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class HeDieuHanhDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.Cpu}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class CpuDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.KetNoi}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class KetNoiDto implements Serializable {
//        @NotBlank
//        String mangDiDong;
//        @NotBlank
//        String sim;
//        Set<WifiDto> wifi;
//        Set<GpsDto> gps;
//        Set<BluetoothDto> bluetooth;
//        @NotBlank
//        String congSac;
//        @NotBlank
//        String jackTaiNghe;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.Wifi}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class WifiDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.Gps}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class GpsDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.Bluetooth}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class BluetoothDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.PinVaSac}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class PinVaSacDto implements Serializable {
//        @NotBlank
//        String dungLuongPin;
//        @NotBlank
//        String loaiPin;
//        @NotBlank
//        String hoTroSacToiDa;
//        Set<CongNghePinDto> congNghePin;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.CongNghePin}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class CongNghePinDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.ThongTinChung}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class ThongTinChungDto implements Serializable {
//        @NotBlank
//        String thietKe;
//        @NotBlank
//        String chatLieu;
//        @NotBlank
//        String kichThuocKhoiLuong;
//        Set<TinhNangDacBietDto> tinhNangDacBiet;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.TinhNangDacBiet}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class TinhNangDacBietDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.KhuyenMai}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class KhuyenMaiDto implements Serializable {
//        Integer id;
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.Series}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class SeriesDto implements Serializable {
//        @Positive
//        Integer id;
//    }
//
//    /**
//     * DTO for {@link com.poly.polystore.entity.SanPhamChiTiet}
//     */
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class SanPhamChiTietDto implements Serializable {
//        Integer id;
//        List<KhuyenMaiDto> khuyenMai;
//        List<AnhDto> anh;
//        @NotNull
//        SanPhamEditRequest.SanPhamChiTietDto.MauSacDto mauSac;
//        @NotNull
//        @NotBlank
//        String rom;
//        @Positive
//        BigDecimal giaBan;
//        @Positive
//        BigDecimal giaNhap;
//        @NotNull
//        SanPhamRepository.TrangThai trangThai;
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.KhuyenMai}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class KhuyenMaiDto implements Serializable {
//            @Positive
//            Integer id;
//        }
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.Anh}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class AnhDto implements Serializable {
//            Integer id;
//            String name;
//        }
//
//        /**
//         * DTO for {@link com.poly.polystore.entity.MauSac}
//         */
//        @AllArgsConstructor
//        @Getter
//        @Setter
//        @NoArgsConstructor
//        public static class MauSacDto implements Serializable {
//            Integer id;
//        }
//    }
}