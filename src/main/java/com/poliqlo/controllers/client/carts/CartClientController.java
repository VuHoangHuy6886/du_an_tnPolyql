package com.poliqlo.controllers.client.carts;

import com.poliqlo.controllers.client.carts.dto.*;
import com.poliqlo.controllers.client.carts.mapper.CartDetailMapper;
import com.poliqlo.controllers.client.carts.service.CartDetailService;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.models.DiaChi;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.PhieuGiamGia;
import com.poliqlo.repositories.DiaChiRepository;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.PhieuGiamGiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CartClientController {
    private final CartDetailService service;
    private final KhachHangRepository khachHangRepository;
    private final DiaChiRepository diaChiRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final AuthService authService;

    @GetMapping("/cart/all")
    public String showCart(Model model) {
        List<CartDetailResponseDTO> responseDTOList = service.getCartDetailByIdCustomer(authService.getCurrentUserDetails().get().getKhachHang().getId());
        model.addAttribute("idCustomer", authService.getCurrentUserDetails().get().getKhachHang().getId());
        model.addAttribute("messageResponse", service.getMessageResponse());
        model.addAttribute("carts", responseDTOList);
        return "client/cart";
    }

    @GetMapping("/cart/increase/{id}")
    public String increase(@PathVariable("id") Integer id, Model model) {
        service.inCreaseByOne(id);
        return "redirect:/cart/all";
    }

    @GetMapping("/cart/decrease/{id}")
    public String decrease(@PathVariable("id") Integer id, Model model) {
        service.deCreaseByOne(id);
        return "redirect:/cart/all";
    }

    @GetMapping("/cart/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Model model) {
        service.delete(id);
        return "redirect:/cart/all";
    }

    @GetMapping("/cart/remove-all/{id}")
    public String removeAll(@PathVariable("id") Integer id, Model model) {
        service.removeAll(id);
        return "redirect:/cart/all";
    }

    @PostMapping("/api/cart/update-quantity")
    public String updateQuantity(@RequestBody QuantityRequest request) {
        service.updateQuantity(request.getId(), request.getQuantity());
        return "redirect:/cart/all";
    }

    @PostMapping("/api/cart/get-voucher")
    public ResponseEntity<?> updateQuantity(@RequestBody DataRequest request) {
        BigDecimal tongTien = new BigDecimal(request.getTongTien());
        List<PhieuGiamGia> phieuGiamGias = new ArrayList<>();

        // Lấy danh sách phiếu giảm giá áp dụng cho khách hàng
        List<PhieuGiamGia> DaApDungChoKhachHang = service.timPhieuGiamGiaChoKhachHang(request.getId(), tongTien);

        if (service.existsCouponsNotApplied()) {
            // Lấy danh sách phiếu giảm giá chưa áp dụng cho khách hàng nào
            List<PhieuGiamGia> PhieuGiamGiaAll = service.couponForAllCustomer(tongTien);

            // Thêm tất cả phiếu giảm giá vào danh sách
            phieuGiamGias.addAll(PhieuGiamGiaAll);
        }

        // Thêm các phiếu giảm giá áp dụng riêng cho khách hàng (nếu có)
        phieuGiamGias.addAll(DaApDungChoKhachHang);

        return ResponseEntity.ok(phieuGiamGias);
    }

    @PostMapping("/cart/trang-thanh-toan")
    public String showPaymentPage(@RequestParam("cartIds") String ids,
                                  @RequestParam(value = "voucherId", required = false) String voucherIdStr,
                                  @RequestParam(value = "idKH") Integer customerId,
                                  Model model) {
        BigDecimal totalPrice = BigDecimal.valueOf(0.00);
        Integer voucherId = 0;
        Integer totalQuantity = 0;

        try {
            voucherId = Integer.parseInt(voucherIdStr);
        } catch (NumberFormatException e) {
            voucherId = 0;
        }

        List<Integer> cartIDs = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<CartDetailResponseDTO> responseDTOList = service.gioHangDaChonDeThanhToan(cartIDs);

        if (responseDTOList != null) {
            totalPrice = service.totalPriceCartDetails(responseDTOList);
            for (CartDetailResponseDTO d : responseDTOList) {
                totalQuantity += Integer.parseInt(d.getQuantity());
            }
        }

        // Hiển thị tất cả địa chỉ của khách hàng
        List<DiaChi> diaChiList = diaChiRepository.timKiemDiaChiTheoIdKhachHang(customerId);
        DiaChi diaChi = diaChiRepository.findAddressDefault(customerId);

        // Xử lý Voucher
        Optional<PhieuGiamGia> phieuGiamGiaOpt = phieuGiamGiaRepository.findById(voucherId);
        BigDecimal tongTien = totalPrice;
        PhieuGiamGia phieuGiamGia = null;

        if (phieuGiamGiaOpt.isPresent()) {
            phieuGiamGia = phieuGiamGiaOpt.get();
            tongTien = totalPrice.subtract(phieuGiamGia.getGiamToiDa());
        }

        // Đảm bảo `voucher` luôn được truyền vào Model, ngay cả khi không có voucher hợp lệ
        // set up bill
        BillRequestDTO billRequestDTO = new BillRequestDTO();
        billRequestDTO.setCartDetailIds(ids);
        billRequestDTO.setCustomerId(String.valueOf(customerId));
        billRequestDTO.setVoucherId(String.valueOf(voucherId));
        if (diaChi != null) {
            model.addAttribute("diachi", diaChi);
        }
        model.addAttribute("bill", billRequestDTO);
        model.addAttribute("voucher", phieuGiamGia);
        model.addAttribute("customerId", customerId);
        model.addAttribute("listAddress", diaChiList);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("carts", responseDTOList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("tongTienSauKhiApDungVoucher", tongTien);

        return "client/payment";
    }

    @PostMapping("/cart/save-bill")
    public String saveBill(@ModelAttribute("bill") BillRequestDTO billRequestDTO, Model model) {
        try {
            service.saveBill(billRequestDTO);
        } catch (Exception e) {
            System.out.printf("lỗi thanh toán");
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
