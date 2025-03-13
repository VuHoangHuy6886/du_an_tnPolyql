package com.poliqlo.controllers.client.carts.service;

import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.controllers.client.carts.dto.AddressResponseDTO;
import com.poliqlo.controllers.client.carts.mapper.CartDetailMapper;
import com.poliqlo.models.*;
import com.poliqlo.repositories.DiaChiRepository;
import com.poliqlo.repositories.GioHangChiTietRepository;
import com.poliqlo.repositories.PhieuGiamGiaRepository;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final GioHangChiTietRepository gioHangChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final DiaChiRepository diaChiRepository;
    public static String thongBao = "";

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
}
