package com.poliqlo.controllers.admin.lich_su_mua_hang.controller;

import com.poliqlo.controllers.admin.lich_su_mua_hang.service.HoaDonService;
import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.models.LichSuHoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/lichsumuahang")
public class LichSuMuaHangController {
    private final HoaDonService hoaDonService;

    @Autowired
    public LichSuMuaHangController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    @GetMapping
    public String showOrderHistory(Model model) {
        // Tạm thời hiển thị tất cả đơn hàng, sau này có thể lọc theo khách hàng đăng nhập
        List<HoaDon> hoaDons = hoaDonService.getAllActiveOrders();
        model.addAttribute("hoaDons", hoaDons);
        return "client/lich-su-mua-hang/index";
    }

    @GetMapping("/khachhang/{khachHangId}")
    public String showOrderHistoryByCustomer(@PathVariable Integer khachHangId, Model model) {
        List<HoaDon> hoaDons = hoaDonService.getOrdersByCustomerId(khachHangId);
        model.addAttribute("hoaDons", hoaDons);
        return "client/lich-su-mua-hang/index";
    }

    @GetMapping("/trangthai/{trangThai}")
    public String showOrderHistoryByStatus(@PathVariable String trangThai, Model model) {
        List<HoaDon> hoaDons = hoaDonService.getOrdersByStatus(trangThai);
        model.addAttribute("hoaDons", hoaDons);
        model.addAttribute("trangThai", trangThai);
        return "client/lich-su-mua-hang/index";
    }

    @GetMapping("/chitiet/{hoaDonId}")
    @ResponseBody
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer hoaDonId) {
        Optional<HoaDon> hoaDonOpt = hoaDonService.getOrderById(hoaDonId);

        if (hoaDonOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HoaDon hoaDon = hoaDonOpt.get();
        List<HoaDonChiTiet> chiTiets = hoaDonService.getOrderDetailsWithProducts(hoaDonId);
        List<LichSuHoaDon> lichSu = hoaDonService.getOrderHistoryByOrderId(hoaDonId);

        Map<String, Object> response = new HashMap<>();
        response.put("hoaDon", hoaDon);
        response.put("chiTiets", chiTiets);
        response.put("lichSu", lichSu);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/modal/{hoaDonId}")
    public String getOrderDetailsModal(@PathVariable Integer hoaDonId, Model model) {
        Optional<HoaDon> hoaDonOpt = hoaDonService.getOrderById(hoaDonId);

        if (hoaDonOpt.isEmpty()) {
            return "error/404";
        }
        HoaDon hoaDon = hoaDonOpt.get();
        List<HoaDonChiTiet> chiTiets = hoaDonService.getOrderDetailsWithProducts(hoaDonId);
        List<LichSuHoaDon> lichSu = hoaDonService.getOrderHistoryByOrderId(hoaDonId);

        model.addAttribute("hoaDon", hoaDon);
        model.addAttribute("chiTiets", chiTiets);
        model.addAttribute("lichSu", lichSu);

        return "client/lich-su-mua-hang/modal-content";
    }

//    @PostMapping("/huy/{hoaDonId}")
//    @ResponseBody
//    public ResponseEntity<?> cancelOrder(@PathVariable Integer hoaDonId) {
//        try {
//            boolean success = hoaDonService.cancelOrder(hoaDonId);
//            if (success) {
//                return ResponseEntity.ok().build();
//            } else {
//                return ResponseEntity.badRequest().body("Không thể hủy đơn hàng này");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Lỗi khi hủy đơn hàng: " + e.getMessage());
//        }
//    }
}