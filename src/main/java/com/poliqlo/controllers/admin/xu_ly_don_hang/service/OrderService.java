package com.poliqlo.controllers.admin.xu_ly_don_hang.service;


import com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.*;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private LichSuHoaDonRepository lichSuHoaDonRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;
    @Autowired
    private AuthService authService;

    @PersistenceContext
    private EntityManager entityManager;
    public Optional<HoaDon> getOrderById(Integer id) {
        return hoaDonRepository.findById(id);
    }
    public Optional<HoaDonChiTiet> getSPCTId(Integer id) {
        return hoaDonChiTietRepository.findById(id);
    }

    public void addHistoryLog(Integer orderId, String tieuDe, String moTa, Integer taiKhoanId) {
        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với id " + orderId));

        TaiKhoan taiKhoan = null;
        if (taiKhoanId != null) {
            taiKhoan = taiKhoanRepository.findById(taiKhoanId).orElse(null);
        }
        LichSuHoaDon lichSu = LichSuHoaDon.builder()
                .hoaDon(hoaDon)
                .taiKhoan(taiKhoan)
                .tieuDe(tieuDe)
                .moTa(moTa)
                .thoiGian(LocalDateTime.now())
                .isDeleted(false)
                .build();

        lichSuHoaDonRepository.save(lichSu);
    }
    @Transactional
    public HoaDon updateOrderStatus(Integer hoaDonId, HoaDonStatusUpdateDTO dto, Integer taiKhoanId) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + hoaDonId));
        String oldStatus = hoaDon.getTrangThai();
        String newStatus = dto.getTrangThai();

        // Cập nhật trạng thái và ghi chú vào đơn hàng
        hoaDon.setTrangThai(newStatus);
        hoaDon.setGhiChu(dto.getGhiChu());

        // Xử lý cập nhật tồn kho cho từng chi tiết đơn hàng (nếu có)
        if (hoaDon.getHoaDonChiTiets() != null) {
            for (HoaDonChiTiet detail : hoaDon.getHoaDonChiTiets()) {
                if (!detail.getIsDeleted()) {
                    SanPhamChiTiet productDetail = sanPhamChiTietRepository.findById(detail.getSanPhamChiTiet().getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id "
                                    + detail.getSanPhamChiTiet().getId()));
                    // Nếu khôi phục đơn hàng (từ DA_HUY sang DANG_CHUAN_BI_HANG): trừ số lượng từ kho
                    if ("DA_HUY".equals(oldStatus) && "DANG_CHUAN_BI_HANG".equals(newStatus)) {
                        if (productDetail.getSoLuong() < detail.getSoLuong()) {
                            throw new RuntimeException("Số lượng tồn kho không đủ cho sản phẩm với id "
                                    + detail.getSanPhamChiTiet().getId());
                        }
                        productDetail.setSoLuong(productDetail.getSoLuong() - detail.getSoLuong());
                        sanPhamChiTietRepository.save(productDetail);
                    }
                    // Nếu chuyển sang CHO_CHUYEN_HOAN (trả hàng về kho): cộng số lượng về kho
                    else if ("HOAN_HANG_THANH_CONG".equals(newStatus)) {
                        productDetail.setSoLuong(productDetail.getSoLuong() + detail.getSoLuong());
                        sanPhamChiTietRepository.save(productDetail);
                    }
                }
            }
        }

        HoaDon updated = hoaDonRepository.save(hoaDon);

        // Ghi log lịch sử (sử dụng tiện ích chuyển đổi trạng thái sang tiếng Việt)
        String oldStatusVi = TrangThaiUtils.convertTrangThai(oldStatus);
        String newStatusVi = TrangThaiUtils.convertTrangThai(newStatus);
        String logMsg = "Chuyển trạng thái từ " + oldStatusVi + " -> " + newStatusVi;
        if (dto.getGhiChu() != null && !dto.getGhiChu().isEmpty()) {
            logMsg += ". Ghi chú: " + dto.getGhiChu();
        }
        addHistoryLog(hoaDonId, "Cập nhật trạng thái", logMsg, taiKhoanId);

        return updated;
    }

    public HoaDonChiTiet updateOrderDetailQuantity(Integer orderId, Integer detailId, Integer newQty) {
        HoaDonChiTiet detail = hoaDonChiTietRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng với id " + detailId));

        int oldQty = detail.getSoLuong();
        int diff = newQty - oldQty; // Số lượng thay đổi

        // Lấy đối tượng ProductDetail từ trường sanPhamChiTietId (giả sử ProductDetail có kiểu Integer id)
        SanPhamChiTiet productDetail = sanPhamChiTietRepository.findById(detail.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id " + detail.getId()));

        if (diff > 0) {
            // Nếu khách hàng tăng số lượng, kiểm tra tồn kho
            if (productDetail.getSoLuong() < diff) {
                throw new RuntimeException("Không đủ tồn kho. Tồn kho hiện tại: " + productDetail.getSoLuong());
            }
            productDetail.setSoLuong(productDetail.getSoLuong() - diff);
        } else if (diff < 0) {
            // Nếu khách hàng giảm số lượng, cộng lại phần chênh lệch vào tồn kho
            productDetail.setSoLuong(productDetail.getSoLuong() + (-diff));
        }
        sanPhamChiTietRepository.save(productDetail);
        HoaDon hoaDon = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));

        detail.setSoLuong(newQty);
        HoaDonChiTiet updatedDetail = hoaDonChiTietRepository.save(detail);
        recalcHoaDonTotal(hoaDon);
         Integer idTaiKhoan= authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId();
        String logMsg = String.format("Cập nhật số lượng sản phẩm\"" + productDetail.getSanPham().getTen() + "\" với số lượng ");
        addHistoryLog(orderId, "Cập nhật số lượng", logMsg, idTaiKhoan);

        return updatedDetail;
    }

    // Final delete: xóa hoàn toàn
    public boolean finalDeleteOrderDetail(Integer orderId, Integer detailId) {
        Optional<HoaDonChiTiet> detailOptional = hoaDonChiTietRepository.findById(detailId);
        if(detailOptional.isPresent()){
            HoaDonChiTiet detail = detailOptional.get();
            detail.setIsDeleted(true);
            hoaDonChiTietRepository.save(detail);
            return true;
        }
        return false;
    }

    // Hàm tính lại tổng tiền của đơn hàng dựa trên chi tiết sản phẩm
    private void recalcHoaDonTotal(HoaDon order) {
        BigDecimal sum = BigDecimal.ZERO;
        // Tính tổng tiền từ các chi tiết đơn hàng (chỉ tính các chi tiết chưa bị xóa)
        if (order.getHoaDonChiTiets() != null) {
            for (HoaDonChiTiet detail : order.getHoaDonChiTiets()) {
                if (!detail.getIsDeleted()) {
                    BigDecimal unitPrice = (detail.getGiaKhuyenMai() != null)
                            ? detail.getGiaKhuyenMai() : detail.getGiaGoc();
                    BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(detail.getSoLuong()));
                    sum = sum.add(lineTotal);
                }
            }
        }

        // Lưu tổng tiền trước khi áp dụng phiếu giảm giá
        BigDecimal preDiscountTotal = sum;

        // Áp dụng phiếu giảm giá nếu có và hợp lệ
        PhieuGiamGia voucher = order.getPhieuGiamGia();
        BigDecimal discount = BigDecimal.ZERO;
        if (voucher != null) {
            // Kiểm tra thời gian áp dụng phiếu giảm giá
            LocalDateTime now = LocalDateTime.now();
            if (voucher.getNgayBatDau() != null && voucher.getNgayKetThuc() != null) {
                if (now.isBefore(voucher.getNgayBatDau()) || now.isAfter(voucher.getNgayKetThuc())) {
                    // Phiếu giảm giá hết hạn hoặc chưa bắt đầu
                    voucher = null;
                }
            } else {
                // Nếu thời gian không được thiết lập đầy đủ, coi như voucher không hợp lệ
                voucher = null;
            }

            // Kiểm tra điều kiện hóa đơn tối thiểu
            if (voucher != null && voucher.getHoaDonToiThieu() != null) {
                if (preDiscountTotal.compareTo(voucher.getHoaDonToiThieu()) < 0) {
                    // Tổng tiền đơn hàng không đủ điều kiện sử dụng voucher
                    voucher = null;
                }
            }
        }

        if (voucher != null) {
            discount = computeVoucherDiscount(voucher, preDiscountTotal);
            order.setGiamMaGiamGia(discount);
        } else {
            order.setGiamMaGiamGia(BigDecimal.ZERO);
        }

        // Cộng phí vận chuyển nếu có
        if (order.getPhiVanChuyen() != null) {
            sum = sum.subtract(discount).add(order.getPhiVanChuyen());
        } else {
            sum = sum.subtract(discount);
        }
        order.setTongTien(sum);
    }

    /**
     * Tính số tiền giảm dựa trên phiếu giảm giá và tổng tiền hóa đơn (preDiscountTotal).
     * Nếu loại chiết khấu là "PHAN_TRAM", discount = preDiscountTotal * (giaTriGiam / 100),
     * không vượt quá giamToiDa.
     * Nếu loại chiết khấu là giảm theo tiền, discount = giaTriGiam, không vượt quá giamToiDa.
     */
    private BigDecimal computeVoucherDiscount(PhieuGiamGia voucher, BigDecimal preDiscountTotal) {
        BigDecimal discount = BigDecimal.ZERO;
        if ("PHAN_TRAM".equalsIgnoreCase(voucher.getLoaiHinhGiam())) {
            discount = preDiscountTotal.multiply(voucher.getGiaTriGiam())
                    .divide(BigDecimal.valueOf(100));
            if (discount.compareTo(voucher.getGiamToiDa()) > 0) {
                discount = voucher.getGiamToiDa();
            }
        } else { // Giảm theo số tiền
            discount = voucher.getGiaTriGiam();
            if (discount.compareTo(voucher.getGiamToiDa()) > 0) {
                discount = voucher.getGiamToiDa();
            }
        }
        // Không được giảm quá tổng tiền
        if (discount.compareTo(preDiscountTotal) > 0) {
            discount = preDiscountTotal;
        }
        return discount;
    }


    // Hàm ghi log lịch sử đơn hàng
    private void addHistory(Integer orderId, Integer taiKhoanId, String tieuDe, String moTa) {
        LichSuHoaDon history = new LichSuHoaDon();
        Optional<HoaDon> hd=hoaDonRepository.findById(orderId);
        HoaDon hf=hd.get();
        Optional<TaiKhoan> tk= taiKhoanRepository.findByIdAndIsDeletedFalse(taiKhoanId);
        TaiKhoan hfe=tk.get();
        history.setHoaDon(hf);
        history.setTaiKhoan(hfe);
        history.setTieuDe(tieuDe);
        history.setMoTa(moTa);
        history.setThoiGian(LocalDateTime.now());
        lichSuHoaDonRepository.save(history);
    }

    // Lấy đơn hàng theo ID
    public HoaDon getHoaDon(Integer orderId) {
        return hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng id " + orderId));
    }

    // Cập nhật thông tin đơn hàng (người nhận, phí vận chuyển, ghi chú)
    public HoaDon updateHoaDonInfo(Integer orderId, HoaDon orderData) {
        HoaDon order = getHoaDon(orderId);
        order.setTenNguoiNhan(orderData.getTenNguoiNhan());
        order.setDiaChi(orderData.getDiaChi());
        order.setSoDienThoai(orderData.getSoDienThoai());
        order.setPhiVanChuyen(orderData.getPhiVanChuyen());
        order.setGhiChu(orderData.getGhiChu());
        recalcHoaDonTotal(order);
        HoaDon updated = hoaDonRepository.save(order);
        addHistory(orderId,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId(), "Cập nhật thông tin", "Cập nhật thông tin người nhận và phí vận chuyển");
        return updated;
    }

    public HoaDon updateOrderInfo(Integer orderId, OrderUpdateInfoDTO dto) {
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));

        // Cập nhật thông tin từ DTO
        if (dto.getTenNguoiNhan() != null && !dto.getTenNguoiNhan().trim().isEmpty()) {
            order.setTenNguoiNhan(dto.getTenNguoiNhan());
        }
        if (dto.getDiaChi() != null && !dto.getDiaChi().trim().isEmpty()) {
            order.setDiaChi(dto.getDiaChi());
        }
        if (dto.getSoDienThoai() != null && !dto.getSoDienThoai().trim().isEmpty()) {
            order.setSoDienThoai(dto.getSoDienThoai());
        }
        if (dto.getPhiVanChuyen() != null) {
            order.setPhiVanChuyen(dto.getPhiVanChuyen());
        }
        if (dto.getGhiChu() != null && !dto.getGhiChu().trim().isEmpty()) {
            order.setGhiChu(dto.getGhiChu());
        }

        // Tính lại tổng tiền (nếu cần tính toán dựa trên phí vận chuyển, giảm giá, ...)
        ;
recalcHoaDonTotal(order);
        HoaDon updatedOrder = hoaDonRepository.save(order);

        // Ghi log lịch sử cập nhật
        String logMsg = "Cập nhật thông tin người nhận thành";
        addHistoryLog(orderId, "Cập nhật thông tin", logMsg,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());

        return updatedOrder;
    }



    /**
     * Hủy đơn hàng, tính lại tổng tiền và ghi log lịch sử.
     *
     * @param orderId ID của đơn hàng cần hủy.
     * @return Đơn hàng đã được cập nhật.
     */
    public HoaDon cancelOrder(Integer orderId) {
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));
        // Lưu trạng thái cũ nếu cần cho log
        String oldStatus = order.getTrangThai();
        // Cập nhật trạng thái đơn hàng
        order.setTrangThai("DA_HUY");
        // Cập nhật tồn kho: với mỗi chi tiết đơn hàng (không bị xóa), cộng lại số lượng
        if (order.getHoaDonChiTiets() != null) {
            for (HoaDonChiTiet detail : order.getHoaDonChiTiets()) {
                if (!detail.getIsDeleted()) {
                    // Lấy đối tượng ProductDetail từ productDetailRepository theo ID của sản phẩm chi tiết
                    SanPhamChiTiet productDetail = sanPhamChiTietRepository.findById(detail.getSanPhamChiTiet().getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id " + detail.getSanPhamChiTiet().getId()));
                    // Cộng lại số lượng đã đặt vào tồn kho
                    productDetail.setSoLuong(productDetail.getSoLuong() + detail.getSoLuong());
                    sanPhamChiTietRepository.save(productDetail);
                }
            }
        }
//
//        // Tính lại tổng tiền của đơn hàng (nếu có logic khác khi hủy)
//        recalcOrderTotal(order);

        HoaDon updatedOrder = hoaDonRepository.save(order);
        // Ghi log lịch sử: chuyển trạng thái, cập nhật tồn kho
        String logMsg = "Hủy đơn hàng: chuyển trạng thái từ "
                + TrangThaiUtils.convertTrangThai(oldStatus)
                + " sang Đã hủy";
        addHistoryLog(orderId, "Hủy đơn hàng", logMsg,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());
        return updatedOrder;
    }
    // Xóa sản phẩm (soft delete) và log
    @Transactional
    public HoaDonChiTiet deleteOrderDetail(Integer orderId, Integer detailId) {
        // Lấy đơn hàng
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));

        // Lấy chi tiết đơn hàng
        HoaDonChiTiet detail = hoaDonChiTietRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng với id " + detailId));

        // Lấy sản phẩm chi tiết
        SanPhamChiTiet sanPhamChiTiet = detail.getSanPhamChiTiet();
        if (sanPhamChiTiet == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm chi tiết tương ứng với đơn hàng");
        }

        // Cộng lại số lượng sản phẩm về kho
        int updatedStock = sanPhamChiTiet.getSoLuong() + detail.getSoLuong();
        sanPhamChiTiet.setSoLuong(updatedStock);
        sanPhamChiTietRepository.save(sanPhamChiTiet); // Cập nhật kho

        // Đánh dấu chi tiết đơn hàng là đã xóa (soft delete)
        detail.setIsDeleted(true);
        hoaDonChiTietRepository.save(detail);

        // Cập nhật lại tổng tiền của đơn hàng
        recalcHoaDonTotal(order);
        hoaDonRepository.save(order);

        // Xây dựng log message
        String productName = (detail.getSanPhamChiTiet().getSanPham().getTen() != null) ? detail.getSanPhamChiTiet().getSanPham().getTen() : "";
        String color = (detail.getSanPhamChiTiet().getMauSac().getTen() != null) ? detail.getSanPhamChiTiet().getMauSac().getTen() : "";
        String size = (detail.getSanPhamChiTiet().getKichThuoc().getTen() != null) ? detail.getSanPhamChiTiet().getKichThuoc().getTen() : "";
        String logMsg = "Xóa sản phẩm \"" + productName + " - " + color + " - " + size + "\"";
        addHistoryLog(orderId, "Xóa sản phẩm", logMsg,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());
        return detail;
    }
    @Transactional
    public HoaDonChiTiet undoDeleteOrderDetail(Integer orderId, Integer detailId) {
        // Lấy đơn hàng
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));

        // Lấy chi tiết đơn hàng
        HoaDonChiTiet detail = hoaDonChiTietRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng với id " + detailId));

        // Kiểm tra chi tiết đơn hàng có đang ở trạng thái xóa không
        if (!detail.getIsDeleted()) {
            throw new RuntimeException("Chi tiết đơn hàng không ở trạng thái bị xóa, không cần hoàn tác.");
        }

        // Lấy sản phẩm chi tiết liên quan
        SanPhamChiTiet sanPhamChiTiet = detail.getSanPhamChiTiet();
        if (sanPhamChiTiet == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm chi tiết tương ứng với đơn hàng");
        }

        // Kiểm tra tồn kho trước khi hoàn tác:
        // Khi xóa, số lượng chi tiết đã được cộng vào tồn kho.
        // Hoàn tác sẽ cần trừ số lượng đó khỏi tồn kho, vì sản phẩm sẽ được phục hồi vào đơn hàng.
        // Do đó, nếu tồn kho hiện tại (số lượng còn) < số lượng cần trừ, không đủ để hoàn tác.
        if (sanPhamChiTiet.getSoLuong() < detail.getSoLuong()) {
            throw new RuntimeException("Không đủ tồn kho để hoàn tác. Tồn kho hiện tại: " + sanPhamChiTiet.getSoLuong());
        }

        // Trừ số lượng từ tồn kho (vì hoàn tác sẽ đưa sản phẩm trở lại đơn hàng)
        sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - detail.getSoLuong());
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        // Đánh dấu chi tiết đơn hàng là chưa bị xóa
        detail.setIsDeleted(false);
        hoaDonChiTietRepository.save(detail);
        // Tính lại tổng tiền của đơn hàng
        recalcHoaDonTotal(order);
        hoaDonRepository.save(order);

        // Xây dựng log message
        String productName = (sanPhamChiTiet.getSanPham().getTen() != null) ? sanPhamChiTiet.getSanPham().getTen() : "";
        String color = (sanPhamChiTiet.getMauSac() != null && sanPhamChiTiet.getMauSac().getTen() != null) ? sanPhamChiTiet.getMauSac().getTen() : "";
        String size = (sanPhamChiTiet.getKichThuoc() != null && sanPhamChiTiet.getKichThuoc().getTen() != null) ? sanPhamChiTiet.getKichThuoc().getTen() : "";
        String logMsg = "Hoàn tác sản phẩm \"" + productName + " - " + color + " - " + size + "\"";

        // Ghi log lịch sử (sử dụng ID tài khoản từ authService)
        Integer userId = authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId();
        addHistoryLog(orderId, "Hoàn tác sản phẩm", logMsg, userId);

        return detail;
    }

    public List<LichSuHoaDonDTO> getLichSuHoaDon(Integer hoaDonId) {
        // Lấy danh sách LichSuHoaDon từ DB
        List<LichSuHoaDon> list = lichSuHoaDonRepository
                .findByHoaDonIdAndIsDeletedFalseOrderByThoiGianDesc(hoaDonId);

        return list.stream().map(ls -> {
            LichSuHoaDonDTO dto = new LichSuHoaDonDTO();
            dto.setId(ls.getId());
            dto.setTieuDe(ls.getTieuDe());
            dto.setMoTa(ls.getMoTa());
            dto.setThoiGian(ls.getThoiGian());
            if (ls.getTaiKhoan() != null) {
                dto.setTaiKhoanId(ls.getTaiKhoan().getId());
                dto.setTenTaiKhoan(ls.getTaiKhoan().getUsername());
            }
            if (ls.getHoaDon() != null) {
                dto.setHoaDonId(ls.getHoaDon().getId());
                dto.setTrangThaiHoaDon(ls.getHoaDon().getTrangThai());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    // Ghi nhận lịch sử hóa đơn
    public void addLichSuHoaDon(Integer hoaDonId, String tieuDe, String moTa, Integer taiKhoanId) {
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findById(hoaDonId);
        if (!hoaDonOptional.isPresent()) {
            throw new RuntimeException("Không tìm thấy hóa đơn");
        }

        HoaDon hoaDon = hoaDonOptional.get();
        TaiKhoan taiKhoan = taiKhoanId != null ? taiKhoanRepository.findById(taiKhoanId).orElse(null) : null;

        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(hoaDon);
        lichSu.setTaiKhoan(taiKhoan);
        lichSu.setTieuDe(tieuDe);
        lichSu.setMoTa(moTa);
        lichSu.setThoiGian(LocalDateTime.now());
        lichSu.setIsDeleted(false);

        lichSuHoaDonRepository.save(lichSu);
    }

    @Transactional
    public HoaDonChiTiet addOrUpdateOrderDetail(Integer orderId, AddProductDetailDTO dto) {
        // Lấy đơn hàng
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));

        // Lấy sản phẩm chi tiết (ProductDetail) theo dto.idSanPhamChiTiet
        SanPhamChiTiet productDetail = sanPhamChiTietRepository.findById(dto.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id " + dto.getIdSanPhamChiTiet()));

        // Kiểm tra tồn kho
        if (productDetail.getSoLuong() < dto.getSoLuong()) {
            throw new RuntimeException("Không đủ tồn kho. Tồn kho hiện tại: " + productDetail.getSoLuong());
        }

        // Kiểm tra xem sản phẩm chi tiết đã có trong đơn hàng chưa
        HoaDonChiTiet existingDetail = null;
        if (order.getHoaDonChiTiets() != null) {
            existingDetail = order.getHoaDonChiTiets()
                    .stream()
                    .filter(od -> od.getSanPhamChiTiet().getId().equals(dto.getIdSanPhamChiTiet()) && !od.getIsDeleted())
                    .findFirst()
                    .orElse(null);
        }

        if (existingDetail != null) {
            // Nếu đã có, cộng dồn số lượng
            int newQuantity = existingDetail.getSoLuong() + dto.getSoLuong();
            existingDetail.setSoLuong(newQuantity);
            // Nếu cần, cập nhật lại giá (giá gốc và giá khuyến mãi) theo DTO
            existingDetail.setGiaGoc(dto.getGiaGoc());
            existingDetail.setGiaKhuyenMai(dto.getGiaKhuyenMai());
            hoaDonChiTietRepository.save(existingDetail);

            // Trừ số lượng mới được thêm khỏi tồn kho
            productDetail.setSoLuong(productDetail.getSoLuong() - dto.getSoLuong());
            sanPhamChiTietRepository.save(productDetail);

            // Ghi log lịch sử
            String logMsg = "Cập nhật số lượng sản phẩm \"" + productDetail.getSanPham().getTen() + "\" từ "
                    + (newQuantity - dto.getSoLuong()) + " lên " + newQuantity;
            addHistoryLog(orderId, "Cập nhật số lượng sản phẩm", logMsg, authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());

            // Cập nhật tổng tiền đơn hàng (nếu cần)
            // recalcOrderTotal(order); -> nếu có logic tính lại tổng tiền
            recalcHoaDonTotal(order);
            hoaDonRepository.save(order);
            return existingDetail;
        } else {
            // Nếu chưa có, tạo mới OrderDetail
            HoaDonChiTiet newDetail = new HoaDonChiTiet();
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(dto.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id: " + dto.getIdSanPhamChiTiet()));
            newDetail.setHoaDon(order);
            newDetail.setSanPhamChiTiet(spct);
            newDetail.setSoLuong(dto.getSoLuong());
            newDetail.setGiaGoc(dto.getGiaGoc());
            newDetail.setGiaKhuyenMai(dto.getGiaKhuyenMai());
            newDetail.setIsDeleted(false);
            hoaDonChiTietRepository.save(newDetail);

            // Trừ số lượng khỏi tồn kho
            productDetail.setSoLuong(productDetail.getSoLuong() - dto.getSoLuong());
            sanPhamChiTietRepository.save(productDetail);

            // Ghi log lịch sử
            String logMsg = "Thêm mới sản phẩm \"" + productDetail.getSanPham().getTen() + "\" với số lượng " + dto.getSoLuong();
            addHistoryLog(orderId, "Thêm sản phẩm", logMsg,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());

            entityManager.refresh(order);
            // Tính lại tổng tiền, bao gồm cả phí giảm giá và phí vận chuyển
            recalcHoaDonTotal(order);
            hoaDonRepository.save(order);
            // Cập nhật tổng tiền đơn hàng
            // recalcOrderTotal(order); -> nếu có logic tính lại tổng tiền

            return newDetail;
        }

    }
    @Transactional
    public HoaDon returnOrder(Integer orderId) {
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));
        // Cập nhật trạng thái sang "CHO_CHUYEN_HOAN"
        order.setTrangThai("CHO_CHUYEN_HOAN");

        // Cập nhật tồn kho: với mỗi chi tiết đơn hàng chưa bị xóa, cộng số lượng về kho.
        if (order.getHoaDonChiTiets() != null) {
            for (HoaDonChiTiet detail : order.getHoaDonChiTiets()) {
                if (!detail.getIsDeleted()) {
                    SanPhamChiTiet productDetail = sanPhamChiTietRepository.findById(detail.getSanPhamChiTiet().getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id "
                                    + detail.getSanPhamChiTiet().getId()));
                    productDetail.setSoLuong(productDetail.getSoLuong() + detail.getSoLuong());
                    sanPhamChiTietRepository.save(productDetail);
                }
            }
        }
        // Ghi log lịch sử
        String logMsg = "Hoàn hàng ";
        addHistoryLog(orderId, "Hoàn trả sản phẩm", logMsg,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());
        hoaDonRepository.save(order);
        return order;
    }

    @Transactional
    public HoaDon restoreOrder(Integer orderId) {
        HoaDon order = hoaDonRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với id " + orderId));
        String oldStatus = order.getTrangThai();
        if (!"DA_HUY".equals(oldStatus)) {
            throw new RuntimeException("Đơn hàng không ở trạng thái hủy nên không thể khôi phục");
        }

        // Khôi phục đơn hàng: chuyển sang "DANG_CHUAN_BI_HANG"
        order.setTrangThai("DANG_CHUAN_BI_HANG");

        // Cập nhật tồn kho: với mỗi chi tiết đơn hàng, trừ số lượng đã cộng về kho khi hủy.
        if (order.getHoaDonChiTiets() != null) {
            for (HoaDonChiTiet detail : order.getHoaDonChiTiets()) {
                if (!detail.getIsDeleted()) {
                    SanPhamChiTiet productDetail = sanPhamChiTietRepository.findById(detail.getSanPhamChiTiet().getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id "
                                    + detail.getSanPhamChiTiet().getId()));
                    if (productDetail.getSoLuong() < detail.getSoLuong()) {
                        throw new RuntimeException("Số lượng tồn kho không đủ cho sản phẩm với id "
                                + detail.getSanPhamChiTiet().getId());
                    }
                    productDetail.setSoLuong(productDetail.getSoLuong() - detail.getSoLuong());
                    sanPhamChiTietRepository.save(productDetail);
                }
            }
        }
        String logMsg = "Khôi phục đơn hàng có id: "+order.getId();
        addHistoryLog(orderId, "Khôi phục đơn hàng", logMsg,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());
//        hoaDonRepository.save(order);
//        entityManager.refresh(order);
        // Tính lại tổng tiền, bao gồm cả phí giảm giá và phí vận chuyển
        recalcHoaDonTotal(order);
        hoaDonRepository.save(order);
        return order;
    }
}

