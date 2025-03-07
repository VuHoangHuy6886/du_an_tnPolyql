package com.poliqlo.controllers.client.carts.service;

import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.controllers.client.carts.mapper.CartDetailMapper;
import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.GioHangChiTiet;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.GioHangChiTietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final GioHangChiTietRepository gioHangChiTietRepository;

    // show card detail by id customer
    public List<CartDetailResponseDTO> getCartDetailByIdCustomer(Integer id) {
        List<GioHangChiTiet> cartsByIdCustomer = gioHangChiTietRepository.gioHangChiTietFindByIdKH(id);
        List<CartDetailResponseDTO> cartDetailResponseDTOList = new ArrayList<>();
        if (cartsByIdCustomer != null) {
            for (GioHangChiTiet cartDetail : cartsByIdCustomer) {
                DotGiamGia discount = this.getLatestDiscountForProduct(cartDetail.getSanPhamChiTiet().getId());
                BigDecimal giaSauKhiGiam = (discount != null)
                        ? this.calculatingPriceOfProductDetail(cartDetail.getSanPhamChiTiet(), discount)
                        : cartDetail.getSanPhamChiTiet().getGiaBan();

                CartDetailResponseDTO responseDTO = CartDetailMapper.mapToDTO(cartDetail, discount, giaSauKhiGiam);
                cartDetailResponseDTOList.add(responseDTO);
            }
        }
        return cartDetailResponseDTOList;
    }


    // The function for calculating the price of a product after applying a discount
    public BigDecimal calculatingPriceOfProductDetail(SanPhamChiTiet sanPhamChiTiet, DotGiamGia dotGiamGia) {
        // default price of productdetail
        BigDecimal priceDefault = sanPhamChiTiet.getGiaBan();
        // lay ra loai hinh giam gia cua dot giam gia
        String discountType = dotGiamGia.getLoaiChietKhau();
        // lay ra gia tri giam
        BigDecimal discountValue = dotGiamGia.getGiaTriGiam();
        // lay ra giam toi da
        BigDecimal discountMax = dotGiamGia.getGiamToiDa();
        // gia cuoi cung
        BigDecimal priceFinal;
        // bây giờ chia làm 2 trường hợp để sử lý
        if (discountType.equals("PHAN_TRAM")) {
            // Chuyển BigDecimal sang int
            int discountValueInt = discountValue.stripTrailingZeros().intValueExact();
            System.out.println(discountValueInt);
            // Kiểm tra nếu discount là 100%
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
        BigDecimal finalDiscount = giaGoc.subtract(actualDiscount);
        return finalDiscount;
    }

    public BigDecimal resultCalculationMoney(BigDecimal soTienGiam, BigDecimal giamToiDa, BigDecimal giaGoc) {
        BigDecimal actualDiscount = soTienGiam.min(giamToiDa);
        BigDecimal finalDiscount = giaGoc.subtract(actualDiscount);
        return finalDiscount;
    }


    // the function checkout discount for product detail
    public DotGiamGia getLatestDiscountForProduct(Integer idProductDetail) {
        return gioHangChiTietRepository.findLatestDiscountForProduct(idProductDetail)
                .orElse(null);
    }
}
