package com.poliqlo.controllers.client.carts;

import com.poliqlo.controllers.client.carts.dto.*;
import com.poliqlo.controllers.client.carts.mapper.CartDetailMapper;
import com.poliqlo.controllers.client.carts.service.CartDetailService;
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

    @GetMapping("/cart/all")
    public String showCart(Model model) {
        List<CartDetailResponseDTO> responseDTOList = service.getCartDetailByIdCustomer(1);
        model.addAttribute("idCustomer", 1);
        model.addAttribute("thongbao", service.thongBao);
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
        List<PhieuGiamGia> phieuGiamGias = service.timPhieuGiamGiaChoKhachHang(request.getId(), tongTien);
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

        model.addAttribute("bill",billRequestDTO);
        model.addAttribute("voucher", phieuGiamGia);
        model.addAttribute("customerId", customerId);
        model.addAttribute("listAddress", diaChiList);
        model.addAttribute("diachi", diaChi);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("carts", responseDTOList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("tongTienSauKhiApDungVoucher", tongTien);

        return "client/payment";
    }


    @PostMapping("/api/cart/dia-chi")
    public ResponseEntity<?> getDiaChiById(@RequestBody Integer id) {
        AddressResponseDTO addressDTO = service.findDiaChi(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/api/cart/list-dia-chi")
    public ResponseEntity<?> geListtDiaChiById(@RequestBody Integer id) {
        List<AddressResponseDTO> addressDTO = service.findAllDiaChi(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/api/cart/add-address")
    public ResponseEntity<?> createAddress(@RequestBody AddressRequestDTO requestDTO) {
        KhachHang khachHang = khachHangRepository.findById(requestDTO.getCustomerID()).get();
        DiaChi diaChi = CartDetailMapper.requestToDiaChi(requestDTO, khachHang);
        diaChiRepository.save(diaChi);
        System.out.printf("Thêm thành công");
        return ResponseEntity.ok("thêm thành công");
    }
    @PostMapping("/cart/save-bill")
    public String saveBill (@ModelAttribute("bill") BillRequestDTO billRequestDTO, Model model) {
        try{
            service.saveBill(billRequestDTO);
        }
        catch (Exception e) {
            System.out.printf("lỗi thanh toán");
            e.printStackTrace();
        }

        return "redirect:/";
    }
}
