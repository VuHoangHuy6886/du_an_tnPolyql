package com.poliqlo.controllers.admin.khach_hang.controller;

import com.poliqlo.controllers.admin.nhan_vien.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api/khach-hang")
public class KhachHangApiController {
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email, @RequestParam(required = false) Integer excludeId) {
        boolean exists = taiKhoanService.existsByEmailExcludeId(email, excludeId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone, @RequestParam(required = false) Integer excludeId) {
        boolean exists = taiKhoanService.existsBySoDienThoaiExcludeId(phone, excludeId);
        return ResponseEntity.ok(exists);
    }
}
