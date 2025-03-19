package com.poliqlo.controllers.admin.khach_hang.controller;

import com.poliqlo.controllers.admin.khach_hang.service.KhachHangService;
import com.poliqlo.controllers.admin.nhan_vien.service.TaiKhoanService;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.NhanVien;
import com.poliqlo.models.TaiKhoan;
import com.poliqlo.repositories.KhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/khach-hang")
public class KhachHangController {
    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private BlobStoreService blobStoreService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/list-khach-hang")
    public String listKhachHang(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> pageKhachHang = khachHangService.getAllKhachHangNotDeleted(pageable);  // Truy vấn phân trang

        model.addAttribute("khachHangs", pageKhachHang.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageKhachHang.getTotalPages());
        model.addAttribute("totalItems", pageKhachHang.getTotalElements());

        return "admin/khach-hang/list-khach-hang";
    }
    @GetMapping("/add-khach-hang")
    public String showAddKhachHang(Model model) {
        model.addAttribute("khachHang", new KhachHang());
        model.addAttribute("taiKhoan", new TaiKhoan());
        return "admin/khach-hang/add-khach-hang";
    }
    @PostMapping("/add-khach-hang")
    public String addKhachHang(@RequestParam("ten") String ten,
                               @RequestParam("ngaySinh") LocalDate ngaySinh,
                               @RequestParam("gioiTinh") String gioiTinh,
                              @RequestParam("email") String email,
                              @RequestParam("soDienThoai") String soDienThoai,
                              @RequestParam("anhUrl") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
//        // Kiểm tra email đã tồn tại
//        if (taiKhoanService.existsByEmail(email)) {
//            redirectAttributes.addFlashAttribute("errorEmail", "Email đã tồn tại, vui lòng nhập email khác.");
//            return "redirect:/admin/khach-hang/add-khach-hang";
//        }
//
//        // Kiểm tra số điện thoại đã tồn tại
//        if (taiKhoanService.existsBySoDienThoai(soDienThoai)) {
//            redirectAttributes.addFlashAttribute("errorPhone", "Số điện thoại đã tồn tại, vui lòng nhập số khác.");
//            return "redirect:/admin/khach-hang/add-khach-hang";
//        }

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setEmail(email);
        taiKhoan.setSoDienThoai(soDienThoai);
        if (!file.isEmpty()) {
            try {
                var blobResponse = blobStoreService.upload(file);
                taiKhoan.setAnhUrl(blobResponse.getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        taiKhoanService.saveTaiKhoan(taiKhoan);
        KhachHang khachHang = new KhachHang();
        khachHang.setTen(ten);
        khachHang.setGioiTinh(gioiTinh);
        khachHang.setNgaySinh(ngaySinh);
        khachHang.setTaiKhoan(taiKhoan);
        khachHangService.saveKhachHang(khachHang);

        redirectAttributes.addFlashAttribute("successMessage", "Thêm khách hàng thành công!");
        return "redirect:/admin/khach-hang/list-khach-hang";
    }
    @GetMapping("/xem-chi-tiet/{id}")
    public String detailKhachHang(@PathVariable("id") Integer id, Model model) {
        KhachHang khachHang = khachHangService.findById(id);
        if (khachHang == null) {
            return "redirect:/admin/khach-hang/list-khach-hang";
        }
        model.addAttribute("khachHang", khachHang);
        return "admin/khach-hang/xem-chi-tiet";
    }
    @GetMapping("/update-khach-hang/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        KhachHang khachHang = khachHangService.findById(id);
        if (khachHang == null) {
            return "redirect:/admin/khach-hang/list-khach-hang";
        }
        model.addAttribute("khachHang", khachHang);
        return "admin/khach-hang/update-khach-hang";
    }
    @GetMapping("/delete-khach-hang/{id}")
    public String deleteKhachHang(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            khachHangService.deleteKhachHang(id);
            taiKhoanService.deleteTaiKhoan(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa khách hàng thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/khach-hang/list-khach-hang";
    }
    @PostMapping("/update-khach-hang/{id}")
    public String updateKhachHang(@PathVariable Integer id,
                                 @ModelAttribute("khachHang") KhachHang khachHang,
                                 @RequestParam(value = "anhUrl", required = false) MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            khachHangService.updateKhachHang(id, khachHang, file);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật khach hang thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/khach-hang/list-khach-hang";
    }
    // Danh sách khach hang bị xóa
    @GetMapping("/list-customer-deleted")
    public String listDeletedKhachHang(Model model) {
        List<KhachHang> danhSachKhachHangDaXoa = khachHangService.getAllKhachHangDeleted();
        model.addAttribute("khachHangs", danhSachKhachHangDaXoa);
        return "admin/khach-hang/list-customer-deleted";
    }
    @GetMapping("/restore/{id}")
    public String restoreNhanVien(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            khachHangService.restoreKhachHang(id);
            redirectAttributes.addFlashAttribute("successMessage", "Khôi phục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể khôi phục");
        }
        return "redirect:/admin/khach-hang/list-customer-deleted";
    }
}
