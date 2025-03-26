package com.poliqlo.controllers.client.carts.service;

import com.poliqlo.controllers.client.carts.dto.BillRequestDTO;
import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.controllers.client.carts.dto.MessageResponse;
import com.poliqlo.controllers.client.carts.mapper.BillMapper;
import com.poliqlo.controllers.client.carts.mapper.CartDetailMapper;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final DiaChiRepository diaChiRepository;
    private final KhachHangRepository khachHangRepository;
    public static String thongBao = "";
    public static MessageResponse messageResponse = new MessageResponse();
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final AuthService authService;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    // show card detail by id customer
    public List<CartDetailResponseDTO> getCartDetailByIdCustomer(Integer id) {
        List<GioHangChiTiet> cartsByIdCustomer = gioHangChiTietRepository.gioHangChiTietFindByIdKH(id);
        List<CartDetailResponseDTO> cartDetailResponseDTOList = new ArrayList<>();
        if (cartsByIdCustomer != null) {
            for (GioHangChiTiet cartDetail : cartsByIdCustomer) {
                DotGiamGia discount = this.getLatestDiscountForProductDetail(cartDetail.getSanPhamChiTiet().getId());
                String url = cartDetail.getSanPhamChiTiet().getSanPham().getAnhUrl();
                BigDecimal giaSauKhiGiam = (discount != null)
                        ? this.calculatingPriceOfProductDetail(cartDetail.getSanPhamChiTiet(), discount)
                        : cartDetail.getSanPhamChiTiet().getGiaBan();
                CartDetailResponseDTO responseDTO = CartDetailMapper.mapToDTO(cartDetail, discount, giaSauKhiGiam, url);
                cartDetailResponseDTOList.add(responseDTO);
            }
        }
        return cartDetailResponseDTOList;
    }

    public List<CartDetailResponseDTO> gioHangDaChonDeThanhToan(List<Integer> ids) {
        List<GioHangChiTiet> cartsByIdCustomer = gioHangChiTietRepository.findByListGioHangIds(ids);
        List<CartDetailResponseDTO> cartDetailResponseDTOList = new ArrayList<>();
        if (cartsByIdCustomer != null) {
            for (GioHangChiTiet cartDetail : cartsByIdCustomer) {
                DotGiamGia discount = this.getLatestDiscountForProductDetail(cartDetail.getSanPhamChiTiet().getId());
                String url = cartDetail.getSanPhamChiTiet().getSanPham().getAnhUrl();
                BigDecimal giaSauKhiGiam = (discount != null)
                        ? this.calculatingPriceOfProductDetail(cartDetail.getSanPhamChiTiet(), discount)
                        : cartDetail.getSanPhamChiTiet().getGiaBan();
                CartDetailResponseDTO responseDTO = CartDetailMapper.mapToDTO(cartDetail, discount, giaSauKhiGiam, url);
                cartDetailResponseDTOList.add(responseDTO);
            }
        }
        return cartDetailResponseDTOList;
    }

    // The function for calculating the price of a product after applying a discount
    public BigDecimal calculatingPriceOfProductDetail(SanPhamChiTiet sanPhamChiTiet, DotGiamGia dotGiamGia) {
        BigDecimal priceDefault = sanPhamChiTiet.getGiaBan();
        String discountType = dotGiamGia.getLoaiChietKhau();
        BigDecimal discountValue = dotGiamGia.getGiaTriGiam();
        BigDecimal discountMax = dotGiamGia.getGiamToiDa();
        BigDecimal priceFinal;
        if (discountType.equals("PHAN_TRAM")) {
            int discountValueInt = discountValue.stripTrailingZeros().intValueExact();
            if (discountValueInt == 100) {
                priceFinal = this.resultCalculationPercent(discountValueInt, discountMax, priceDefault);
            } else {
                priceFinal = this.resultCalculationPercent(discountValueInt, discountMax, priceDefault);
            }
        } else {
            priceFinal = this.resultCalculationMoney(discountValue, discountMax, priceDefault);
        }
        return priceFinal;
    }

    public BigDecimal resultCalculationPercent(int phanTram, BigDecimal giamToiDa, BigDecimal giaGoc) {
        BigDecimal discountAmount = giaGoc.multiply(BigDecimal.valueOf(phanTram)).divide(BigDecimal.valueOf(100));
        BigDecimal actualDiscount = discountAmount.min(giamToiDa);
        BigDecimal giaSauGiam = giaGoc.subtract(actualDiscount);

        // Đảm bảo giá sau giảm không nhỏ hơn 0
        return giaSauGiam.max(BigDecimal.ZERO);
    }

    public BigDecimal resultCalculationMoney(BigDecimal soTienGiam, BigDecimal giamToiDa, BigDecimal giaGoc) {
        BigDecimal actualDiscount = soTienGiam.min(giamToiDa);
        BigDecimal giaSauGiam = giaGoc.subtract(actualDiscount);

        // Đảm bảo giá sau giảm không nhỏ hơn 0
        return giaSauGiam.max(BigDecimal.ZERO);
    }

    // the function checkout discount for product detail
    public DotGiamGia getLatestDiscountForProductDetail(Integer idProductDetail) {
        return gioHangChiTietRepository.findLatestDiscountForProduct(idProductDetail)
                .orElse(null);
    }

    public void deCreaseByOne(Integer id) {
        GioHangChiTiet cartDetail = gioHangChiTietRepository.findById(id).orElse(null);
        if (cartDetail != null) {
            Integer newQuantity = cartDetail.getSoLuong() - 1;
            if (newQuantity < 1) {
                messageResponse.setMessage("số lượng trong giỏ hàng tối thiểu là 1");
                messageResponse.setSuccess(false);
            } else {
                cartDetail.setSoLuong(newQuantity);
                gioHangChiTietRepository.save(cartDetail);
                thongBao = "Giảm Số lượng thành công";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(true);
            }
        }
    }

    public void inCreaseByOne(Integer id) {
        GioHangChiTiet cartDetail = gioHangChiTietRepository.findById(id).orElse(null);
        thongBao = "";
        if (cartDetail != null) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(cartDetail.getSanPhamChiTiet().getId()).orElse(null);
            Integer defaultQuantity = sanPhamChiTiet.getSoLuong();

            Integer newQuantity = cartDetail.getSoLuong() + 1;
            // tong tien
            BigDecimal totalMoney = BigDecimal.valueOf(newQuantity).multiply(sanPhamChiTiet.getGiaBan());
            // So sánh với 5,000,000
            BigDecimal limit = BigDecimal.valueOf(5000000);
            if (newQuantity > defaultQuantity) {
                thongBao = "số thêm đã vượt quá số luong của cửa hàng";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(false);
            } else if (totalMoney.compareTo(limit) >= 0) {
                thongBao = "số thêm không được vượt quá 5 triệu đồng !";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(false);
            } else {
                cartDetail.setSoLuong(newQuantity);
                gioHangChiTietRepository.save(cartDetail);
                thongBao = "Thêm Số Lượng Thành Công";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(true);
            }
        }
    }

    public void updateQuantity(Integer id, Integer quantity) {
        GioHangChiTiet cartDetail = gioHangChiTietRepository.findById(id).orElse(null);
        thongBao = "";
        if (cartDetail != null) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(cartDetail.getSanPhamChiTiet().getId()).orElse(null);
            Integer defaultQuantity = sanPhamChiTiet.getSoLuong();
            // tong tien
            BigDecimal totalMoney = BigDecimal.valueOf(quantity).multiply(sanPhamChiTiet.getGiaBan());
            // So sánh với 5,000,000
            BigDecimal limit = BigDecimal.valueOf(5000000);
            if (quantity > defaultQuantity) {
                thongBao = "số thêm đã quá số lượng trong kho - số lượng kho là : " + sanPhamChiTiet.getSoLuong();
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(false);
            } else if (quantity < 1) {
                thongBao = "số sản phẩm trong giỏ hang phải lơn  hơn hoặc bằng 1";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(false);
            } else if (totalMoney.compareTo(limit) >= 0) {
                thongBao = "số thêm không được vượt quá 5 triệu đồng !";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(false);
            } else {
                cartDetail.setSoLuong(quantity);
                gioHangChiTietRepository.save(cartDetail);
                thongBao = "Thêm Số Lượng Thành Công";
                messageResponse.setMessage(thongBao);
                messageResponse.setSuccess(true);
            }
        }
    }

    public void delete(Integer id) {
        GioHangChiTiet cartDetail = gioHangChiTietRepository.findById(id).orElse(null);
        cartDetail.setIsDeleted(true);
        gioHangChiTietRepository.save(cartDetail);
        thongBao = "Delete Thành Công giỏ hang có id : " + id;
    }

    public void removeAll(Integer id) {
        List<GioHangChiTiet> carts = gioHangChiTietRepository.gioHangChiTietFindByIdKH(id);
        for (GioHangChiTiet cartDetail : carts) {
            cartDetail.setIsDeleted(true);
            gioHangChiTietRepository.save(cartDetail);
        }
        thongBao = "xóa thành công !";
    }

    public List<PhieuGiamGia> timPhieuGiamGiaChoKhachHang(Integer id, BigDecimal tongTien) {
        return phieuGiamGiaRepository.hienThiPhieuGiamBangIdKhachHang(id, tongTien);
    }

    public List<PhieuGiamGia> couponForAllCustomer(BigDecimal tongTien) {
        return phieuGiamGiaRepository.findCouponsNotApplied(tongTien);
    }

    public Boolean existsCouponsNotApplied() {
        return phieuGiamGiaRepository.existsCouponsNotApplied();
    }

    // ham tỉnh tổng tền của các giỏ hàng mà khách hàng mua
    public BigDecimal totalPriceCartDetails(List<CartDetailResponseDTO> lists) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartDetailResponseDTO item : lists) {
            total = total.add(new BigDecimal(item.getTotalPrice())); // Chuyển đổi và cộng dồn
        }
        return total;
    }

    // thanh toán tao hoa don
    @Transactional
    public void saveBill(BillRequestDTO billRequestDTO) {
        // 1. Lấy danh sách ID của các sản phẩm trong giỏ hàng
        List<Integer> cartIDs = Arrays.stream(billRequestDTO.getCartDetailIds().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByListGioHangIds(cartIDs);

        // 2. Lấy thông tin khách hàng
        KhachHang khachHang = khachHangRepository.findById(Integer.parseInt(billRequestDTO.getCustomerId())).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // 3. Kiểm tra và lấy thông tin phiếu giảm giá nếu có
        PhieuGiamGia phieuGiamGia = null;
        String voucherIdStr = billRequestDTO.getVoucherId();
        if (voucherIdStr != null && !voucherIdStr.isEmpty() && Integer.parseInt(voucherIdStr) != 0) {
            PhieuGiamGia pggKho = phieuGiamGiaRepository.findById(Integer.parseInt(voucherIdStr))
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu giảm giá"));
            if (pggKho.getSoLuong() < 1) {
                throw new RuntimeException("Phiếu Giảm : " + pggKho.getMa() + " - Đã hết số lượng hoặc hiệu lực vui lòng chọn lại");
            }
            phieuGiamGia = pggKho;
        }

        // 4. Kiểm tra số lượng sản phẩm trong giỏ hàng trước khi tạo hóa đơn
        for (GioHangChiTiet check : gioHangChiTietList) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(check.getSanPhamChiTiet().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));
            if (check.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Sản phẩm : " + sanPhamChiTiet.getSanPham().getTen()
                        + " -  màu : " + sanPhamChiTiet.getMauSac().getTen()
                        + " - kích thước : " + sanPhamChiTiet.getKichThuoc().getTen()
                        + " - số lượng không đủ số lượng trong kho còn : " + sanPhamChiTiet.getSoLuong()
                );
            }
        }

        // 5. Lấy thông tin địa chỉ giao hàng
        DiaChi diaChi = diaChiRepository.findById(Integer.parseInt(billRequestDTO.getAddressId()))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

        // 6. Tạo và lưu hóa đơn
        HoaDon hoaDon = BillMapper.BillRequestToBill(billRequestDTO, phieuGiamGia, khachHang, diaChi);
        hoaDonRepository.save(hoaDon);
        // su ly lich su hoa don
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setTaiKhoan(authService.getCurrentUserDetails().get());
        lichSuHoaDon.setTieuDe(HoaDonRepository.CHO_XAC_NHAN);
        lichSuHoaDon.setMoTa("Đơn hàng vừa được tạo ra, chờ xác nhận từ người bán");
        lichSuHoaDon.setThoiGian(LocalDateTime.now());
        lichSuHoaDon.setIsDeleted(false);
        lichSuHoaDonRepository.save(lichSuHoaDon);

        // 7. Cập nhật số lượng phiếu giảm giá (nếu có)
        if (phieuGiamGia != null) {
            phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
            phieuGiamGiaRepository.save(phieuGiamGia);
        }

        // 8. Xử lý giỏ hàng và cập nhật thông tin hóa đơn chi tiết
        for (GioHangChiTiet gh : gioHangChiTietList) {
            // Lấy thông tin sản phẩm chi tiết
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(gh.getSanPhamChiTiet().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

            // Lấy đợt giảm giá nếu có
            DotGiamGia dotGiamGia = this.getLatestDiscountForProductDetail(sanPhamChiTiet.getId());

            // Xác định giá sau khuyến mãi
            BigDecimal giaKhuyenMai = (dotGiamGia != null) ? this.calculatingPriceOfProductDetail(sanPhamChiTiet, dotGiamGia) : sanPhamChiTiet.getGiaBan();
            BigDecimal giaGoc = sanPhamChiTiet.getGiaBan();
            Integer soLuong = gh.getSoLuong();

            // Tạo và lưu hóa đơn chi tiết
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setGiaGoc(giaGoc);
            hoaDonChiTiet.setGiaKhuyenMai(giaKhuyenMai);
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setIsDeleted(false);
            if (dotGiamGia != null) {
                hoaDonChiTiet.setDotGiamGia(dotGiamGia);
            }
            hoaDonChiTietRepository.save(hoaDonChiTiet);

            // 9. Cập nhật số lượng sản phẩm trong kho
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - soLuong);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            // su ly san pham chi tiet khi so luong = 0
//            if (sanPhamChiTiet.getSoLuong() == 0){
//                sanPhamChiTiet.se
//                sanPhamChiTietRepository.save(sanPhamChiTiet);
//            }

            // 10. Ẩn sản phẩm khỏi giỏ hàng chi tiết
            gh.setIsDeleted(true);
            gioHangChiTietRepository.save(gh);
        }

    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }
}
