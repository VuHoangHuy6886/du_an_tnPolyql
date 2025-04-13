    package com.poliqlo.controllers.admin.lich_su_mua_hang.service;

    import com.poliqlo.controllers.admin.hoa_don.service.DiaLyService;
    import com.poliqlo.models.*;
    import com.poliqlo.repositories.HoaDonChiTietRepository;
    import com.poliqlo.repositories.HoaDonRepository;
    import com.poliqlo.repositories.LichSuHoaDonRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class HoaDonService {
        @Autowired
        private DiaLyService diaLyService;

        private final HoaDonRepository hoaDonRepository;
        private final HoaDonChiTietRepository hoaDonChiTietRepository;
        private final LichSuHoaDonRepository lichSuHoaDonRepository;

        @Autowired
        public HoaDonService(HoaDonRepository hoaDonRepository,
                             HoaDonChiTietRepository hoaDonChiTietRepository,
                             LichSuHoaDonRepository lichSuHoaDonRepository) {
            this.hoaDonRepository = hoaDonRepository;
            this.hoaDonChiTietRepository = hoaDonChiTietRepository;
            this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        }

        public List<HoaDon> getAllActiveOrders() {
            return hoaDonRepository.findAllActiveOrders();
        }

        // Thêm phương thức mới hỗ trợ phân trang
        public Page<HoaDon> getAllActiveOrdersPaged(Pageable pageable) {
            Page<HoaDon> hoaDonPage = hoaDonRepository.findAllActiveOrdersPaged(pageable);
            capNhatDiaChiDayDuChoHoaDon(hoaDonPage.getContent());
            return hoaDonPage;
        }

        public List<HoaDon> getOrdersByCustomerId(Integer khachHangId) {
            return hoaDonRepository.findAllByKhachHangId(khachHangId);
        }

        // Thêm phương thức mới hỗ trợ phân trang
        public Page<HoaDon> getOrdersByCustomerIdPaged(Integer khachHangId, Pageable pageable) {
            return hoaDonRepository.findAllByKhachHangIdPaged(khachHangId, pageable);
        }

        public List<HoaDon> getOrdersByStatus(String trangThai) {
            return hoaDonRepository.findAllByTrangThai(trangThai);
        }

        // Thêm phương thức mới hỗ trợ phân trang
        public Page<HoaDon> getOrdersByStatusPaged(String trangThai, Pageable pageable) {
            Page<HoaDon> hoaDonPage = hoaDonRepository.findAllByTrangThaiPaged(trangThai, pageable);
            capNhatDiaChiDayDuChoHoaDon(hoaDonPage.getContent());
            return hoaDonPage;
        }

        public List<HoaDon> getOrdersByCustomerIdAndStatus(Integer khachHangId, String trangThai) {
            return hoaDonRepository.findAllByKhachHangIdAndTrangThai(khachHangId, trangThai);
        }

        // Thêm phương thức mới hỗ trợ phân trang
        public Page<HoaDon> getOrdersByCustomerIdAndStatusPaged(Integer khachHangId, String trangThai, Pageable pageable) {
            return hoaDonRepository.findAllByKhachHangIdAndTrangThaiPaged(khachHangId, trangThai, pageable);
        }

        // Cập nhật phương thức getOrderById để bao gồm địa chỉ đầy đủ
        public Optional<HoaDon> getOrderById(Integer hoaDonId) {
            Optional<HoaDon> hoaDonOpt = hoaDonRepository.findActiveById(hoaDonId);

            hoaDonOpt.ifPresent(hoaDon -> {
                // Lấy và cập nhật địa chỉ đầy đủ
                String diaChiDayDu = layDiaChiDayDu(hoaDon);
                hoaDon.setDiaChi(diaChiDayDu);
            });

            return hoaDonOpt;
        }


        public List<HoaDonChiTiet> getOrderDetailsByOrderId(Integer hoaDonId) {
            return hoaDonChiTietRepository.findAllByHoaDonId(hoaDonId);
        }

        public List<HoaDonChiTiet> getOrderDetailsWithProducts(Integer hoaDonId) {
            return hoaDonChiTietRepository.findAllDetailsByHoaDonId(hoaDonId);
        }

        public List<LichSuHoaDon> getOrderHistoryByOrderId(Integer hoaDonId) {
            return lichSuHoaDonRepository.findAllByHoaDonId(hoaDonId);
        }

        public Page<HoaDon> searchByOrderId(Integer id, Pageable pageable) {
            return hoaDonRepository.findByIdPaged(id, pageable);
        }

        public Page<HoaDon> searchByPriceRange(BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable) {
            return hoaDonRepository.findByPriceRangePaged(minAmount, maxAmount, pageable);
        }

        public Page<HoaDon> searchByDateRange(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
            return hoaDonRepository.findByDateRangePaged(fromDate, toDate, pageable);
        }

        public Page<HoaDon> searchOrders(
                String orderId,
                String trangThai,
                String loaiHoaDon,
                BigDecimal minAmount,
                BigDecimal maxAmount,
                LocalDateTime fromDate,
                LocalDateTime toDate,
                Pageable pageable) {
            Integer orderIdNumber = null;
            if (orderId != null && !orderId.isEmpty()) {
                // Nếu orderId bắt đầu bằng "hd" hoặc "HD", cắt bỏ phần đó và chuyển phần còn lại thành số
                if (orderId.toLowerCase().startsWith("hd")) {
                    try {
                        orderIdNumber = Integer.parseInt(orderId.substring(2));
                    } catch (NumberFormatException e) {
                        // Xử lý khi không thể chuyển đổi thành số
                        // Ở đây có thể return một trang trống hoặc xử lý khác tùy nhu cầu
                        return Page.empty(pageable);
                    }
                } else {
                    // Nếu chỉ là một số, thử chuyển đổi trực tiếp
                    try {
                        orderIdNumber = Integer.parseInt(orderId);
                    } catch (NumberFormatException e) {
                        // Xử lý khi không thể chuyển đổi thành số
                        return Page.empty(pageable);
                    }
                }
            }

            Page<HoaDon> hoaDonPage = hoaDonRepository.searchOrdersPaged(orderIdNumber, trangThai, loaiHoaDon, minAmount, maxAmount, fromDate, toDate, pageable);
            capNhatDiaChiDayDuChoHoaDon(hoaDonPage.getContent());
            return hoaDonPage;
        }

        public Page<HoaDon> getRecentOrders(int days, Pageable pageable) {
            // Lấy ngày hiện tại, trừ đi số ngày cần xem, và chuyển sang LocalDateTime
            LocalDateTime fromDate = LocalDate.now().minusDays(days).atStartOfDay();
            return hoaDonRepository.findRecentOrdersPaged(fromDate, pageable);
        }

        public List<HoaDon> getOrdersByLoaiHoaDon(String loaiHoaDon) {
            return hoaDonRepository.findAllByLoaiHoaDon(loaiHoaDon);
        }

        public Page<HoaDon> getOrdersByLoaiHoaDonPaged(String loaiHoaDon, Pageable pageable) {
            return hoaDonRepository.findAllByLoaiHoaDonPaged(loaiHoaDon, pageable);
        }

        // Thêm phương thức mới để lấy đơn hàng theo nhiều trạng thái
        public Page<HoaDon> getOrdersByMultipleStatusesPaged(List<String> statuses, Pageable pageable) {
            Page<HoaDon> hoaDonPage = hoaDonRepository.findAllByTrangThaiInPaged(statuses, pageable);
            capNhatDiaChiDayDuChoHoaDon(hoaDonPage.getContent());
            return hoaDonPage;
        }
        public boolean cancelOrder(Integer hoaDonId, String lyDoHuy) {
            Optional<HoaDon> hoaDonOpt = hoaDonRepository.findActiveById(hoaDonId);

            if (hoaDonOpt.isEmpty()) {
                return false;
            }

            HoaDon hoaDon = hoaDonOpt.get();


            if (!hoaDon.getTrangThai().equals(HoaDonRepository.CHO_XAC_NHAN)) {
                return false;
            }


            hoaDon.setTrangThai(HoaDonRepository.DA_HUY);
            hoaDon.setGhiChu(lyDoHuy);

            LichSuHoaDon lichSuHoaDon = LichSuHoaDon.builder()
                    .hoaDon(hoaDon)
                    .tieuDe("Hủy đơn hàng")
                    .moTa("Đơn hàng đã bị hủy. Lý do: " + lyDoHuy)
                    .thoiGian(LocalDateTime.now())
                    .isDeleted(false)
                    .build();

            hoaDonRepository.save(hoaDon);
            lichSuHoaDonRepository.save(lichSuHoaDon);

            return true;
        }
        public String layDiaChiDayDu(HoaDon hoaDon) {
            if (hoaDon == null) {
                return "";
            }

            // Nếu không có khách hàng, trả về địa chỉ trong HoaDon (nếu có)
            if (hoaDon.getKhachHang() == null) {
                return hoaDon.getDiaChi() != null ? hoaDon.getDiaChi() : "";
            }

            KhachHang khachHang = hoaDon.getKhachHang();
            // Tìm địa chỉ khớp với thông tin người nhận và số điện thoại của HoaDon
            Optional<DiaChi> diaChiOpt = khachHang.getDanhSachDiaChi().stream()
                    .filter(dc -> !dc.getIsDeleted()
                            && dc.getSoDienThoai() != null && dc.getSoDienThoai().equals(hoaDon.getSoDienThoai())
                            && dc.getHoTenNguoiNhan() != null && dc.getHoTenNguoiNhan().equals(hoaDon.getTenNguoiNhan()))
                    .findFirst();

            // Nếu không tìm thấy địa chỉ khớp với số điện thoại và người nhận, tìm địa chỉ khớp với diaChi
            if (diaChiOpt.isEmpty() && hoaDon.getDiaChi() != null && !hoaDon.getDiaChi().isEmpty()) {
                diaChiOpt = khachHang.getDanhSachDiaChi().stream()
                        .filter(dc -> !dc.getIsDeleted()
                                && dc.getAddress() != null && dc.getAddress().equals(hoaDon.getDiaChi()))
                        .findFirst();
            }

            // Nếu vẫn không tìm thấy, lấy địa chỉ mặc định
            if (diaChiOpt.isEmpty()) {
                diaChiOpt = khachHang.getDanhSachDiaChi().stream()
                        .filter(dc -> !dc.getIsDeleted() && dc.getIsDefault())
                        .findFirst();
            }

            if (diaChiOpt.isPresent()) {
                DiaChi diaChi = diaChiOpt.get();
                return diaLyService.ghepDiaChiDayDu(
                        diaChi.getAddress(),
                        diaChi.getWardCode(),
                        diaChi.getDistrictId(),
                        diaChi.getProvinceId());
            }

            // Nếu không tìm thấy địa chỉ nào, trả về địa chỉ trong HoaDon (nếu có)
            return hoaDon.getDiaChi() != null ? hoaDon.getDiaChi() : "";
        }

        private void capNhatDiaChiDayDuChoHoaDon(List<HoaDon> hoaDons) {
            for (HoaDon hoaDon : hoaDons) {
                String diaChiDayDu = layDiaChiDayDu(hoaDon);
                hoaDon.setDiaChi(diaChiDayDu);
            }
        }
    }