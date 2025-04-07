package com.poliqlo.controllers.admin.lich_su_mua_hang.controller;

import com.poliqlo.controllers.admin.lich_su_mua_hang.service.HoaDonService;
import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.models.LichSuHoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/lichsumuahang")
public class LichSuMuaHangController {
    private final HoaDonService hoaDonService;
    private static final int PAGE_SIZE = 2; // Số đơn hàng trên 1 trang

    @Autowired
    public LichSuMuaHangController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }
    // Hiển thị các đơn hàng
    @GetMapping
    public String showOrderHistory(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage = hoaDonService.getAllActiveOrdersPaged(pageable);

        addPaginationAttributes(model, hoaDonPage);

        return "client/lich-su-mua-hang/index";
    }
     // Lọc theo ID khách hàng
    @GetMapping("/khachhang/{khachHangId}")
    public String showOrderHistoryByCustomer(
            @PathVariable Integer khachHangId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage = hoaDonService.getOrdersByCustomerIdPaged(khachHangId, pageable);

        addPaginationAttributes(model, hoaDonPage);
        model.addAttribute("khachHangId", khachHangId);

        return "client/lich-su-mua-hang/index";
    }

    // Lọc đơn hàng theo trạng thái
    @GetMapping("/trangthai/{trangThai}")
    public String showOrderHistoryByStatus(
            @PathVariable String trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage ;
        // Kiểm tra nếu trangThai chứa nhiều trạng thái (phân tách bằng dấu phẩy)
        if (trangThai.contains(",")) {
            List<String> statuses = Arrays.asList(trangThai.split(","));
            hoaDonPage = hoaDonService.getOrdersByMultipleStatusesPaged(statuses, pageable);
        } else {
            hoaDonPage = hoaDonService.getOrdersByStatusPaged(trangThai, pageable);
        }

        addPaginationAttributes(model, hoaDonPage);
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

    // Phương thức hỗ trợ để thêm thuộc tính phân trang vào model
    private void addPaginationAttributes(Model model, Page<HoaDon> hoaDonPage) {
        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", hoaDonPage.getNumber());
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("totalItems", hoaDonPage.getTotalElements());
        model.addAttribute("pageSize", PAGE_SIZE);
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