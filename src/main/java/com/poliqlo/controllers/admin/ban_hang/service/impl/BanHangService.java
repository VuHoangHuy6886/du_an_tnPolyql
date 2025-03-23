package com.poliqlo.controllers.admin.ban_hang.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BanHangService {
//    private final ModelMapper modelMapper;
//    private final KhachHangRepository khachHangRepository;
//    private final HoaDonRepository hoaDonRepository;
//    private final LichSuHoaDonRepository lichSuHoaDonRepository;
//    private final NhanVienRepository nhanVienRepository;
//    private final LichSuBaoHanhRepository lichSuBaoHanhRepository;
//    private final ThongTinBaoHanhRepository thongTinBaoHanhRepository;
//    private final SanPhamChiTietRepository sanPhamChiTietRepository;
//    private final ImeiRepository imeiRepository;
//
//    @Override
//    public ResponseEntity<?> save(HoaDonTaiQuayAddRequest req) {
//        var rp = new HoaDonTaiQuayAddRequest();
//        var hoaDon = new HoaDon();
//        try {
//            KhachHang khachHang = new KhachHang();
//            if (req.getKhachHang().getId() != null) {
//                khachHang = khachHangRepository.findById(req.getKhachHang().getId()).orElse(null);
//            }
//            khachHang.setEmail(req.getKhachHang().getEmail());
//            khachHang.setSoDienThoai(req.getKhachHang().getSoDienThoai());
//            khachHang.setTen(req.getKhachHang().getTen());
//            hoaDon.setKhachHang(khachHangRepository.save(khachHang));
//        } catch (PersistenceException pe) {
//            return ResponseEntity.badRequest().body("Kiểm tra lại thông tin khách hàng");
//        }
//        hoaDon.setCreatedAt(Instant.now());
//        hoaDon.setLastModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
//        hoaDon.setThoiGianNhanHang(Instant.now());
//        hoaDon.setHinhThucGiaoHang("Tại quầy");
//        hoaDon.setLichSuHoaDons(List.of(new LichSuHoaDon()));
//        hoaDon.setEmail(req.getEmail());
//        hoaDon.setSoDienThoai(req.getSoDienThoai());
//        hoaDon.setGiamVoucher(req.getGiamVoucher());
//        hoaDon.setNote(req.getNote());
//        hoaDon.setTrangThai(TRANGTHAIDONHANG.THANH_CONG);
//        hoaDon.setTongTienHoaDon(req.getTongTienHoaDon());
//        hoaDon.setHinhThucThanhToan(req.getHinhThucThanhToan());
//        hoaDon.setMaGiamGia(req.getMaGiamGiaId());
//        hoaDon.setTenNguoiNhan(req.getTenNguoiNhan());
//        hoaDon.setTongSanPham(req.getTongSanPham());
//        hoaDon.setPhiGiaoHang(BigDecimal.ZERO);
//        hoaDon.setTrangThaiThanhToan("Đã thanh toán");
//        hoaDon.setNhanVien(nhanVienRepository.findByEmail(((TaiKhoan) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail()));
//        var resp = hoaDonRepository.save(hoaDon);
//        lichSuHoaDonRepository.save(new LichSuHoaDon(resp.getId(), "Hoàn tất hóa đơn", "Hóa đơn đã được tạo và thanh toán bởi " + hoaDon.getNhanVien().getMaNhanVien()));
//        for(String imei:req.getDanhSachImei()){
//            var oldImei= imeiRepository.findById(imei).get();
//            oldImei.setTrangThai(Imei.TrangThai.DA_BAN);
//            imeiRepository.save(oldImei);
//            var sanPhamChiTiet=oldImei.getSanPhamChiTiet();
//            var soLuong=sanPhamChiTiet.getSoLuong()-1;
//            sanPhamChiTiet.setSoLuong(Math.max(soLuong, 0));
//            sanPhamChiTietRepository.save(sanPhamChiTiet);
//            var thoiGianBaoHanh=LocalDateTime.now().plusMonths(Long.parseLong(imeiRepository.findById(imei).get().getSanPhamChiTiet().getSanPham().getThoiGianBaoHanh().replaceAll("\\D","")));
//            var thongTinBaoHanh= thongTinBaoHanhRepository.save(new ThongTinBaoHanh(imei,hoaDon.getKhachHang(),thoiGianBaoHanh.toInstant(ZoneOffset.UTC)));
//            lichSuBaoHanhRepository.save(new LichSuBaoHanh(thongTinBaoHanh,Instant.now(),"Bảo hành đã được kích hoạt"));
//        }
//        return ResponseEntity.ok().body(resp);
//    }
}
