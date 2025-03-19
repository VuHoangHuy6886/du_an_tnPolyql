package com.poliqlo.controllers.admin.nhan_vien.controller;


import com.poliqlo.controllers.admin.nhan_vien.service.NhanVienService;
import com.poliqlo.controllers.admin.nhan_vien.service.TaiKhoanService;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.NhanVien;
import com.poliqlo.models.TaiKhoan;
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

@Controller
@RequestMapping("admin/nhan-vien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;
    @Autowired
        private TaiKhoanService taiKhoanService;

    @Autowired
    private BlobStoreService blobStoreService;
    // Hiển thị danh sách nhân viên
    @GetMapping("/list")
    public String listNhanVien(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NhanVien> pageNhanVien = nhanVienService.getAllNhanVienNotDeleted(pageable);  // Truy vấn phân trang

        model.addAttribute("nhanViens", pageNhanVien.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageNhanVien.getTotalPages());
        model.addAttribute("totalItems", pageNhanVien.getTotalElements());

        return "admin/nhan-vien/list";
    }

    @GetMapping("add-nhan-vien")
    public String showAddForm(Model model) {
         model.addAttribute("nhanVien", new NhanVien());
         model.addAttribute("taiKhoan", new TaiKhoan());
        return "admin/nhan-vien/add-nhan-vien";
    }
    @PostMapping("/add-nhan-vien")
    public String addNhanVien(@RequestParam("ten") String ten,
                              @RequestParam("email") String email,
                              @RequestParam("soDienThoai") String soDienThoai,
                              @RequestParam("role") String role,
                              @RequestParam("password") String password,
                              @RequestParam("anhUrl") MultipartFile file,
                              RedirectAttributes redirectAttributes) {

//        // Kiểm tra email đã tồn tại
//        if (taiKhoanService.existsByEmail(email)) {
//            redirectAttributes.addFlashAttribute("errorEmail", "Email đã tồn tại, vui lòng nhập email khác.");
//            return "redirect:/admin/nhan-vien/add-nhan-vien";
//        }
//
//        // Kiểm tra số điện thoại đã tồn tại
//        if (taiKhoanService.existsBySoDienThoai(soDienThoai)) {
//            redirectAttributes.addFlashAttribute("errorPhone", "Số điện thoại đã tồn tại, vui lòng nhập số khác.");
//            return "redirect:/admin/nhan-vien/add-nhan-vien";
//        }

        // Tạo tài khoản mới
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setEmail(email);
        taiKhoan.setSoDienThoai(soDienThoai);
        taiKhoan.setRole(role);
        taiKhoan.setPassword(password);

        if (!file.isEmpty()) {
            try {
                var blobResponse = blobStoreService.upload(file);
                taiKhoan.setAnhUrl(blobResponse.getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        taiKhoanService.saveTaiKhoan(taiKhoan);
        NhanVien nhanVien = new NhanVien();
        nhanVien.setTen(ten);
        nhanVien.setTaiKhoan(taiKhoan);
        nhanVienService.saveNhanVien(nhanVien);

        redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên thành công!");
        return "redirect:/admin/nhan-vien/list";
    }

    @GetMapping("/xem-chi-tiet/{id}")
    public String detailNhanVien(@PathVariable("id") Integer id, Model model) {
        NhanVien nhanVien = nhanVienService.findById(id);
        if (nhanVien == null) {
            return "redirect:/admin/nhan-vien/list";
        }
        model.addAttribute("nhanVien", nhanVien);
        return "admin/nhan-vien/xem-chi-tiet";
    }

    @GetMapping("/update-nhan-vien/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        NhanVien nhanVien = nhanVienService.findById(id);
        if (nhanVien == null) {
            return "redirect:/admin/nhan-vien/list";
        }
        model.addAttribute("nhanVien", nhanVien);
        return "admin/nhan-vien/update-nhan-vien";  // Trỏ đến file Thymeleaf trên
    }
    @GetMapping("/delete-nhan-vien/{id}")
    public String deleteNhanVien(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            nhanVienService.deleteNhanVien(id);
            taiKhoanService.deleteTaiKhoan(id);
            redirectAttributes.addFlashAttribute("successMessage02", "Xóa nhân viên thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage01", e.getMessage());
        }
        return "redirect:/admin/nhan-vien/list";
    }
    // Danh sách nhân viên bị xóa
    @GetMapping("/list-deleted")
    public String listDeletedNhanVien(Model model,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NhanVien> pageNhanVien = nhanVienService.getAllNhanVienDeleted(pageable);

        model.addAttribute("nhanViens", pageNhanVien.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageNhanVien.getTotalPages());
        model.addAttribute("totalItems", pageNhanVien.getTotalElements());

        return "admin/nhan-vien/list-deleted";
    }
    // Khôi phục nhân viên đã bị xóa
    @GetMapping("/restore/{id}")
    public String restoreNhanVien(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            nhanVienService.restoreNhanVien(id);
            redirectAttributes.addFlashAttribute("successMessage", "Khôi phục nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể khôi phục nhân viên!");
        }
        return "redirect:/admin/nhan-vien/list-deleted";
    }
    @PostMapping("/update-nhan-vien/{id}")
    public String updateNhanVien(@PathVariable Integer id,
                                 @ModelAttribute("nhanVien") NhanVien nhanVien,
                                 @RequestParam(value = "anhUrl", required = false) MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            nhanVienService.updateNhanVien(id, nhanVien, file);
            redirectAttributes.addFlashAttribute("successMessage03", "Cập nhật nhân viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage02", "Lỗi khi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/nhan-vien/list";
    }

}


