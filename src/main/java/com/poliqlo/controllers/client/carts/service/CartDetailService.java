package com.poliqlo.controllers.client.carts.service;

import com.poliqlo.controllers.client.carts.dto.BillRequestDTO;
import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.controllers.client.carts.dto.AddressResponseDTO;
import com.poliqlo.controllers.client.carts.mapper.BillMapper;
import com.poliqlo.controllers.client.carts.mapper.CartDetailMapper;
import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final DotGiamGiaRepository  dotGiamGiaRepository;

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
        return giaGoc.subtract(actualDiscount);
    }

    public BigDecimal resultCalculationMoney(BigDecimal soTienGiam, BigDecimal giamToiDa, BigDecimal giaGoc) {
        BigDecimal actualDiscount = soTienGiam.min(giamToiDa);
        return giaGoc.subtract(actualDiscount);
    }

    // the function checkout discount for product detail
    public DotGiamGia getLatestDiscountForProductDetail(Integer idProductDetail) {
        return gioHangChiTietRepository.findLatestDiscountForProduct(idProductDetail)
                .orElse(null);
    }

    public void deCreaseByOne(Integer id) {
        GioHangChiTiet cartDetail = gioHangChiTietRepository.findById(id).orElse(null);
        thongBao = "";
        if (cartDetail != null) {
            Integer newQuantity = cartDetail.getSoLuong() - 1;
            if (newQuantity < 1) {
                thongBao = "số lượng trong giỏ hàng tối thiểu là 1";
                return;
            } else {
                cartDetail.setSoLuong(newQuantity);
                gioHangChiTietRepository.save(cartDetail);
                thongBao = "Giảm Số lượng thành công";
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

            if (newQuantity >= defaultQuantity) {
                thongBao = "số thêm đã vượt quá số luong của cửa hàng";
            } else {
                cartDetail.setSoLuong(newQuantity);
                gioHangChiTietRepository.save(cartDetail);
                thongBao = "Thêm Số Lượng Thành Công";
            }
        }
    }

    public void updateQuantity(Integer id, Integer quantity) {
        GioHangChiTiet cartDetail = gioHangChiTietRepository.findById(id).orElse(null);
        thongBao = "";
        if (cartDetail != null) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(cartDetail.getSanPhamChiTiet().getId()).orElse(null);
            Integer defaultQuantity = sanPhamChiTiet.getSoLuong();
            if (quantity > defaultQuantity) {
                thongBao = "số thêm đã vượt quá số luong của cửa hàng";
            } else if (quantity < 1) {
                thongBao = "số sản phẩm trong giỏ hang phải lơn  hơn hoặc bằng 1";
            } else {
                cartDetail.setSoLuong(quantity);
                gioHangChiTietRepository.save(cartDetail);
                thongBao = "Thêm Số Lượng Thành Công";
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

    // ham tỉnh tổng tền của các giỏ hàng mà khách hàng mua
    public BigDecimal totalPriceCartDetails(List<CartDetailResponseDTO> lists) {
        BigDecimal total = BigDecimal.ZERO;
        for (CartDetailResponseDTO item : lists) {
            total = total.add(new BigDecimal(item.getTotalPrice())); // Chuyển đổi và cộng dồn
        }
        return total;
    }

    // get dia chi
    public AddressResponseDTO findDiaChi(Integer id) {
        DiaChi diaChi = diaChiRepository.findById(id).get();
        return CartDetailMapper.diaChiToAddress(diaChi);
    }

    public List<AddressResponseDTO> findAllDiaChi(Integer id) {
        List<DiaChi> diachiEntity = diaChiRepository.timKiemDiaChiTheoIdKhachHang(id);
        List<AddressResponseDTO> addressResponseDTOList = new ArrayList<>();
        for (DiaChi dc : diachiEntity) {
            AddressResponseDTO addressDTO = CartDetailMapper.diaChiToAddress(dc);
            addressResponseDTOList.add(addressDTO);
        }
        return addressResponseDTOList;
    }

    // thanh toán tao hoa don
    @Transactional
    public String saveBill (BillRequestDTO billRequestDTO) {
        // list cart detail id
        List<Integer> cartIDs = Arrays.stream(billRequestDTO.getCartDetailIds().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<GioHangChiTiet> gioHangChiTietList = gioHangChiTietRepository.findByListGioHangIds(cartIDs);
        // khach hang
        KhachHang khachHang = khachHangRepository.findById(Integer.parseInt(billRequestDTO.getCustomerId())).get();
        // phieu giam gia
        PhieuGiamGia phieuGiamGia = null;
        String voucherIdStr = billRequestDTO.getVoucherId();
        System.out.println(voucherIdStr);
        if (voucherIdStr != null && Integer.parseInt(voucherIdStr) != 0) {
            PhieuGiamGia check = phieuGiamGiaRepository.findById(Integer.parseInt(voucherIdStr)).get();
            if (check.getSoLuong() < 1){
                throw new RuntimeException("phiếu giảm giá số lượng không đủ");
            }else {
                phieuGiamGia = check;
            }
        }
        // 2. kiem tra so luong san pham trong gio hang trc
        for (GioHangChiTiet check : gioHangChiTietList) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(check.getId()).get();
            if (check.getSoLuong() > sanPhamChiTiet.getSoLuong()){
                throw new RuntimeException("số lượng sản phẩm không đủ không đủ");
            }
        }
        // dia chi
        DiaChi diaChi = diaChiRepository.findById(Integer.parseInt(billRequestDTO.getAddressId())).get();
        HoaDon hoaDon = BillMapper.BillRequestToBill(billRequestDTO,phieuGiamGia,khachHang,diaChi);
        hoaDonRepository.save(hoaDon);
        // tru di 1 phieu giam gia va save
        if (phieuGiamGia != null) {
            Integer soLuongVoucherNew = phieuGiamGia.getSoLuong() - 1;
            phieuGiamGia.setSoLuong(soLuongVoucherNew);
            phieuGiamGiaRepository.save(phieuGiamGia);
        }

        // su ly gio hang va san pham + dot giam gia
        for (GioHangChiTiet gh : gioHangChiTietList) {
            // sản phẩm chi tiết
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(gh.getSanPhamChiTiet().getId()).get();
            // dot giam gia
            DotGiamGia dotGiamGia = this.getLatestDiscountForProductDetail(sanPhamChiTiet.getId());
            // lay gia sau khi giam
            BigDecimal giaKhuyenMai ;
            if (dotGiamGia != null) {
                giaKhuyenMai = this.calculatingPriceOfProductDetail(sanPhamChiTiet,dotGiamGia);
            }else {
                giaKhuyenMai = sanPhamChiTiet.getGiaBan();
            }
            BigDecimal giaGoc = gh.getSanPhamChiTiet().getGiaBan();
            Integer soLuong = gh.getSoLuong();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            if (dotGiamGia != null) {
                hoaDonChiTiet.setDotGiamGia(dotGiamGia);
            }
            hoaDonChiTiet.setGiaGoc(giaGoc);
            hoaDonChiTiet.setGiaKhuyenMai(giaKhuyenMai);
            hoaDonChiTiet.setSoLuong(soLuong);
            hoaDonChiTiet.setIsDeleted(false);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            // tiep theo tru di so luong san pham trong gio hang
            Integer soLuongMoi = sanPhamChiTiet.getSoLuong() - soLuong;
            sanPhamChiTiet.setSoLuong(soLuongMoi);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            // ẩn gio hang chi tiet
            gh.setIsDeleted(true);
            gioHangChiTietRepository.save(gh);
        }
        return "Thêm Thành Công";
    }
}
