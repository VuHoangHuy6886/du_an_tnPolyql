package com.poliqlo.controllers.admin.xu_ly_don_hang.controller;


import com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.AddPhieuGiamGiaRequest;
import com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.HoaDonChiTietDTO;
import com.poliqlo.controllers.admin.xu_ly_don_hang.model.request.UpdatePhieuGiamGiaRequest;
import com.poliqlo.controllers.admin.xu_ly_don_hang.service.OrderService;
import com.poliqlo.models.HoaDon;
import com.poliqlo.models.HoaDonChiTiet;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.PhieuGiamGia;
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

    // Cập nhật trạng thái đơn hàng
    @PostMapping("/{id}/updateStatus")
    public ResponseEntity<HoaDon> updateOrderStatus(@PathVariable Integer id,
                                                    @RequestParam String trangThai) {
        HoaDon updatedOrder = orderService.updateOrderStatus(id, trangThai);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    // Cập nhật số lượng của một chi tiết đơn hàng
    @PostMapping("/{orderId}/details/{detailId}/update")
    public ResponseEntity<HoaDonChiTiet> updateOrderDetail(@PathVariable Integer orderId,
                                                           @PathVariable Integer detailId,
                                                           @RequestParam Integer soLuong) {
        HoaDonChiTiet updatedDetail = orderService.updateOrderDetail(orderId, detailId, soLuong);
        if (updatedDetail != null) {
            return ResponseEntity.ok(updatedDetail);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa (soft delete) sản phẩm khỏi đơn hàng
    @PostMapping("/{orderId}/details/{detailId}/delete")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Integer orderId,
                                                  @PathVariable Integer detailId) {
        boolean result = orderService.deleteOrderDetail(orderId, detailId);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Hoàn tác xóa sản phẩm (undo soft delete)
    @PostMapping("/{orderId}/details/{detailId}/undo")
    public ResponseEntity<HoaDonChiTiet> undoDeleteOrderDetail(@PathVariable Integer orderId,
                                                               @PathVariable Integer detailId) {
        HoaDonChiTiet restoredDetail = orderService.undoDeleteOrderDetail(orderId, detailId);
        if (restoredDetail != null) {
            return ResponseEntity.ok(restoredDetail);
        }
        return ResponseEntity.notFound().build();
    }

    // Xóa hoàn toàn sản phẩm khỏi đơn hàng (final delete)
    @PostMapping("/{orderId}/details/{detailId}/finalDelete")
    public ResponseEntity<Void> finalDeleteOrderDetail(@PathVariable Integer orderId,
                                                       @PathVariable Integer detailId) {
        boolean result = orderService.finalDeleteOrderDetail(orderId, detailId);
        if (result) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
