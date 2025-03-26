package com.poliqlo.controllers.admin.xu_ly_don_hang.controller;


import com.poliqlo.controllers.admin.gio_hang.model.response.Response;
import com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.*;
import com.poliqlo.controllers.admin.xu_ly_don_hang.service.OrderService;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.models.*;
import com.poliqlo.repositories.HoaDonChiTietRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private AuthService authService;

    // Lấy thông tin đơn hàng theo id (bao gồm cả chi tiết)
    @GetMapping("/{id}")
    public ResponseEntity<HoaDon> getOrderById(@PathVariable Integer id) {
        Optional<HoaDon> orderOptional = orderService.getOrderById(id);
        return orderOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/chi-tiet/{hoaDonId}")
    public ResponseEntity<List<HoaDonChiTietDTO>> getAllByHoaDonId(@PathVariable Integer hoaDonId) {
        List<HoaDonChiTietDTO> danhSachChiTiet = hoaDonChiTietRepository.findByHoaDonId(hoaDonId);
        if (danhSachChiTiet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(danhSachChiTiet);
    }
    @GetMapping("/{hoaDonId}/chi-tiet")
    public ResponseEntity<List<HoaDonChiTietDTO>> getChiTietHoaDon(@PathVariable Integer hoaDonId) {
        List<HoaDonChiTietDTO> chiTietList = hoaDonChiTietRepository.findByHoaDonId(hoaDonId);
        return ResponseEntity.ok(chiTietList);
    }

    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{id}/updateStatus")
    public ResponseEntity<HoaDon> updateStatus(@PathVariable Integer id,
                                               @RequestBody HoaDonStatusUpdateDTO dto) {
        HoaDon updated = orderService.updateOrderStatus(id, dto,  authService.getCurrentUserDetails().get().getKhachHang().getTaiKhoan().getId());
        return ResponseEntity.ok(updated);
    }

    // Cập nhật số lượng của một chi tiết đơn hàng
    @PostMapping("/{orderId}/details/{detailId}/update")
    public ResponseEntity<HoaDonChiTiet> updateOrderDetail(@PathVariable Integer orderId,
                                                         @PathVariable Integer detailId,
                                                         @RequestBody HoaDonChiTietUpdateDTO dto) {
        HoaDonChiTiet updatedDetail = orderService.updateOrderDetailQuantity(orderId, detailId, dto.getSoLuong());
        return ResponseEntity.ok(updatedDetail);
    }

    // Xóa (soft delete) sản phẩm khỏi đơn hàng
    @PostMapping("/{orderId}/details/{detailId}/delete")
    public ResponseEntity<HoaDonChiTiet> deleteOrderDetail(@PathVariable Integer orderId,
                                                         @PathVariable Integer detailId) {
        HoaDonChiTiet updatedDetail = orderService.deleteOrderDetail(orderId, detailId);
        return ResponseEntity.ok(updatedDetail);
    }

    // Hoàn tác xóa sản phẩm (undo soft delete)
    @PostMapping("/{orderId}/details/{detailId}/undo")
    public ResponseEntity<HoaDonChiTiet> undoDeleteOrderDetail(@PathVariable Integer orderId,
                                                             @PathVariable Integer detailId) {
        HoaDonChiTiet restoredDetail = orderService.undoDeleteOrderDetail(orderId, detailId);
        return ResponseEntity.ok(restoredDetail);
    }

    @GetMapping("/{orderId}/history")
    public List<LichSuHoaDonDTO> getLichSuHoaDon(@PathVariable Integer orderId) {
        return orderService.getLichSuHoaDon(orderId);
    }

    // Ghi nhận lịch sử
    @PostMapping("/{hoaDonId}")
    public void addLichSuHoaDon(
            @PathVariable Integer hoaDonId,
            @RequestParam String tieuDe,
            @RequestParam String moTa,
            @RequestParam(required = false) Integer taiKhoanId) {
        orderService.addLichSuHoaDon(hoaDonId, tieuDe, moTa, taiKhoanId);
    }

    @PostMapping("/{orderId}/details/{detailId}/finalDelete")
    public ResponseEntity<Void> finalDeleteOrderDetail(@PathVariable Integer orderId,
                                                       @PathVariable Integer detailId) {
        boolean result = orderService.finalDeleteOrderDetail(orderId, detailId);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<HoaDon> cancelHoaDon(@PathVariable Integer orderId) {
        HoaDon updatedOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(updatedOrder);
    }
    @PutMapping("/{orderId}/updateInfo")
    public ResponseEntity<HoaDon> updateOrderInfo(@PathVariable Integer orderId,
                                                 @RequestBody OrderUpdateInfoDTO dto) {
        HoaDon updatedOrder = orderService.updateOrderInfo(orderId, dto);
        return ResponseEntity.ok(updatedOrder);
    }
    @PostMapping("/{orderId}/details/add")
    public ResponseEntity<HoaDonChiTiet> addProductDetail(@PathVariable Integer orderId,
                                                        @RequestBody AddProductDetailDTO dto) {
        HoaDonChiTiet detail = orderService.addOrUpdateOrderDetail(orderId, dto);
        return ResponseEntity.ok(detail);
    }

    @PostMapping("/{orderId}/return")
    public HoaDon returnOrder(@PathVariable Integer orderId) {
        return orderService.returnOrder(orderId);
    }

    @PostMapping("/{orderId}/restore")
    public HoaDon restoreOrder(@PathVariable Integer orderId) {
        return orderService.restoreOrder(orderId);
    }



}
