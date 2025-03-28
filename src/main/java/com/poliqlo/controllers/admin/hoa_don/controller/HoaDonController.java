package com.poliqlo.controllers.admin.hoa_don.controller;

import com.poliqlo.controllers.admin.lich_su_mua_hang.service.HoaDonService;
import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.models.LichSuHoaDon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/admin/hoa-don")
public class HoaDonController {
    private final HoaDonService hoaDonService;
    private static final int PAGE_SIZE = 6; // Số đơn hàng trên 1 trang

    @Autowired
    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }
    // Hiển thị các đơn hàng
    @GetMapping("/index")
    public String showOrderHistory(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime toDate,
            @RequestParam(value = "displayDays", required = false) Integer displayDays,
            @RequestParam(value = "loaiHoaDon", required = false) String loaiHoaDon, // Thêm tham số loaiHoaDon
            Model model) {

        if (loaiHoaDon == null) {
            loaiHoaDon = "ONLINE";
        }

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage ;

        // Nếu có displayDays, ưu tiên lọc theo ngày gần đây
        if (displayDays != null && displayDays > 0) {
            LocalDateTime fromDateFilter = LocalDate.now().minusDays(displayDays).atStartOfDay();
            hoaDonPage = hoaDonService.searchOrders(null, null, loaiHoaDon, null, null, fromDateFilter, null, pageable);
        } else {
            // Sử dụng phương thức searchOrders để lọc theo loaiHoaDon mặc định
            hoaDonPage = hoaDonService.searchOrders(orderId, null, loaiHoaDon, minAmount, maxAmount, fromDate, toDate, pageable);
        }
        // Thêm tất cả tham số tìm kiếm vào model
        model.addAttribute("orderId", orderId);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("displayDays", displayDays);
        model.addAttribute("loaiHoaDon", loaiHoaDon);
        addPaginationAttributes(model, hoaDonPage);

        return "/admin/hoa-don/index";
    }
    @GetMapping("/khachhang/{khachHangId}")
    public String showOrderHistoryByCustomer(
            @PathVariable Integer khachHangId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime toDate,
            @RequestParam(value = "displayDays", required = false) Integer displayDays,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage = hoaDonService.getOrdersByCustomerIdPaged(khachHangId, pageable);

        // Thêm tất cả tham số tìm kiếm vào model
        model.addAttribute("khachHangId", khachHangId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("displayDays", displayDays);

        addPaginationAttributes(model, hoaDonPage);
        return "/admin/hoa-don/index";
    }

    @GetMapping("/trangthai/{trangThai}")
    public String showOrderHistoryByStatus(
            @PathVariable String trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime toDate,
            @RequestParam(value = "displayDays", required = false) Integer displayDays,
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
        // Thêm tất cả tham số tìm kiếm vào model
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("orderId", orderId);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("displayDays", displayDays);

        addPaginationAttributes(model, hoaDonPage);
        return "/admin/hoa-don/index";
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

        return "/admin/hoa-don/modal-content";
    }

    // Phương thức hỗ trợ để thêm thuộc tính phân trang vào model
    private void addPaginationAttributes(Model model, Page<HoaDon> hoaDonPage) {
        model.addAttribute("hoaDons", hoaDonPage.getContent());
        model.addAttribute("currentPage", hoaDonPage.getNumber());
        model.addAttribute("totalPages", hoaDonPage.getTotalPages());
        model.addAttribute("totalItems", hoaDonPage.getTotalElements());
        model.addAttribute("pageSize", PAGE_SIZE);
    }

    @GetMapping("/search")
    public String searchOrders(
            @RequestParam(value = "orderId", required = false) Integer orderId,
            @RequestParam(value = "trangThai", required = false) String trangThai,
            @RequestParam(value = "minAmount", required = false) BigDecimal minAmount,
            @RequestParam(value = "maxAmount", required = false) BigDecimal maxAmount,
            @RequestParam(value = "fromDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd[ HH:mm:ss]") LocalDateTime toDate,
            @RequestParam(value = "displayDays", required = false) Integer displayDays,
            @RequestParam(value = "loaiHoaDon", required = false) String loaiHoaDon,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage;

        // Xử lý fromDate và toDate để thêm giờ mặc định nếu thiếu
        if (fromDate != null && fromDate.getHour() == 0 && fromDate.getMinute() == 0 && fromDate.getSecond() == 0) {
            fromDate = fromDate.withHour(0).withMinute(0).withSecond(0); // Đảm bảo 00:00:00
        }
        if (toDate != null && toDate.getHour() == 0 && toDate.getMinute() == 0 && toDate.getSecond() == 0) {
            toDate = toDate.withHour(23).withMinute(59).withSecond(59); // Đảm bảo 23:59:59
        }

        // Xử lý lọc hiển thị theo số ngày gần đây
        if (displayDays != null && displayDays > 0) {
            hoaDonPage = hoaDonService.getRecentOrders(displayDays, pageable);
        }
        // Tìm kiếm kết hợp
        else {
            // Xử lý giá trị mặc định
            if (minAmount == null) minAmount = BigDecimal.ZERO;
            if (maxAmount == null) maxAmount = new BigDecimal("999999999999");

            hoaDonPage = hoaDonService.searchOrders(orderId, trangThai, loaiHoaDon, minAmount, maxAmount, fromDate, toDate, pageable);
        }

        // Luôn thêm tất cả tham số tìm kiếm vào model để bảo toàn khi chuyển trang
        model.addAttribute("orderId", orderId);
        model.addAttribute("searchTrangThai", trangThai);
        model.addAttribute("loaiHoaDon", loaiHoaDon);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("displayDays", displayDays);


        addPaginationAttributes(model, hoaDonPage);
        return "/admin/hoa-don/index";
    }
    @GetMapping("/loaihoadon/{loaiHoaDon}")
    public String showOrderHistoryByLoaiHoaDon(
            @PathVariable String loaiHoaDon,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<HoaDon> hoaDonPage = hoaDonService.getOrdersByLoaiHoaDonPaged(loaiHoaDon, pageable);

        model.addAttribute("loaiHoaDon", loaiHoaDon);
        addPaginationAttributes(model, hoaDonPage);
        return "/admin/hoa-don/index";
    }


}
