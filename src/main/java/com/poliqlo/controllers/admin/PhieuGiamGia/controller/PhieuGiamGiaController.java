package com.poliqlo.controllers.admin.PhieuGiamGia.controller;


import com.poliqlo.controllers.admin.PhieuGiamGia.model.request.AddPhieuGiamGiaRequest;
import com.poliqlo.controllers.admin.PhieuGiamGia.model.request.UpdatePhieuGiamGiaRequest;
import com.poliqlo.controllers.admin.PhieuGiamGia.service.PhieuGiamGiaService;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.PhieuGiamGia;
import com.poliqlo.repositories.PhieuGiamGiaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/phieu-giam-gia")
public class PhieuGiamGiaController {
    private final PhieuGiamGiaRepository repository;

    private final PhieuGiamGiaService service;


    @GetMapping("/all")
    public String showAll(@RequestParam(value = "page", defaultValue = "0", required = false) String pageStr,
                          @RequestParam(value = "size", defaultValue = "5", required = false) String sizeStr,
                          @RequestParam(value = "name", required = false) String name,
                          @RequestParam(value = "status", required = false) String status,
                          @RequestParam(value = "startTime", required = false) String startTimeStr,
                          @RequestParam(value = "endTime", required = false) String endTimeStr,
                          Model model
    ) {
        Integer page, size;

        try {
            page = Integer.parseInt(pageStr);
            size = Integer.parseInt(sizeStr);
        } catch (NumberFormatException e) {
            page = 0;
            size = 5;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startTime = null, endTime = null;

        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            try {
                startTime = LocalDate.parse(startTimeStr, formatter).atStartOfDay();
            } catch (Exception e) {
                startTime = null;
            }
        }

        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            try {
                endTime = LocalDate.parse(endTimeStr, formatter).atTime(23, 59, 59);
            } catch (Exception e) {
                endTime = null;
            }
        }

        // Kiểm tra nếu tất cả các điều kiện lọc đều trống thì hiển thị tất cả dữ liệu
        if ((name == null || name.isEmpty()) &&
                (status == null || status.isEmpty()) &&
                startTime == null &&
                endTime == null) {
            service.updateStatusCoupon(service.findAllList());
            Page<PhieuGiamGia> pages = service.findAll(page, size);

            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pages.getTotalPages());
            return "admin/phieu-giam-gia/index";
        }
        if (name.equals("")) {
            name = null;
        }
        if (status.equals("")) {
            status = null;
        }

        service.updateStatusCoupon(service.findAllList());
        Page<PhieuGiamGia> pages = service.findAll(page, size, name, status, startTime, endTime);
        if (pages.hasContent()) {
            model.addAttribute("pages", pages.getContent());
        } else {
            model.addAttribute("message", "Không có dữ liệu nào");
        }
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        return "admin/phieu-giam-gia/index";
    }


    @GetMapping("/form-add")
    public String formAdd(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "5") int size,
                          @RequestParam(value = "name", required = false) String search,
                          Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> khachHangPage;
        System.out.println("ten : " + search);
        // Lọc khách hàng theo tên nếu có search
        if (search != null && !search.trim().isEmpty()) {
            khachHangPage = service.timKhachHangTheoTen(search, pageable);
            model.addAttribute("listKH", khachHangPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", khachHangPage.getTotalPages());
            model.addAttribute("request", new AddPhieuGiamGiaRequest());
            return "/admin/phieu-giam-gia/form-add";
        } else {
            khachHangPage = service.getAllCustomers(pageable);
            model.addAttribute("listKH", khachHangPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", khachHangPage.getTotalPages());
            model.addAttribute("request", new AddPhieuGiamGiaRequest());
            return "/admin/phieu-giam-gia/form-add";
        }
    }


    @PostMapping("/save")
    public String add(@ModelAttribute("request") AddPhieuGiamGiaRequest request,
                      @RequestParam("listIdCustomer") String ids,
                      Model model
    ) {


        List<Integer> listCustomer;
        if (!ids.isEmpty() && ids != null) {
            listCustomer = Arrays.stream(ids.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else {
            listCustomer = null;
        }

        if (repository.existsByTen(request.getTen())) {
            model.addAttribute("checkTrungTen", "ten da ton tai");
            return "/admin/phieu-giam-gia/form-add";
        }

        if (repository.existsByGiaTriGiam(BigDecimal.valueOf(Double.parseDouble(request.getGiaTriGiam())))) {
            model.addAttribute("checktrungGTG", "Gia tri giam nay da ton tai");
            return "/admin/phieu-giam-gia/form-add";
        }


        service.save(request, listCustomer);
        return "redirect:/admin/phieu-giam-gia/all";
    }


    @GetMapping("/form-update/{id}")
    public String viewDetail(@PathVariable("id") Integer id,
                             Model model) {

        //lay thong tin cua pgg theo id
        PhieuGiamGia a = service.findById(id);

        // Chỉ hiển thị phần số nếu có giá trị thập phân
        DecimalFormat df = new DecimalFormat("#.##");


        //tao mot doi tuong trong
        UpdatePhieuGiamGiaRequest b = new UpdatePhieuGiamGiaRequest();
        //gan cac thuoc tinh cua entity tim duoc vao entity request
        b.setId(a.getId());
        b.setMa(a.getMa());
        b.setTen(a.getTen());
        b.setSoLuong(String.valueOf(a.getSoLuong()));
        b.setHoaDonToiThieu(df.format(a.getHoaDonToiThieu()));
        b.setLoaiHinhGiam(String.valueOf(a.getLoaiHinhGiam()));
        b.setGiaTriGiam(df.format(a.getGiaTriGiam()));
        b.setGiamToiDa(df.format(a.getGiamToiDa()));
        b.setNgayBatDau(a.getNgayBatDau());
        b.setNgayKetThuc(a.getNgayKetThuc());
        b.setTrangThai(a.getTrangThai());

        //day doi tuong tim thay(da co du lieu) sang view bang keyword "request"
        model.addAttribute("request", b);
        //day du lieu sang form update
        return "admin/phieu-giam-gia/form-update";
    }


    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("request") UpdatePhieuGiamGiaRequest request,
                         BindingResult bindingResult,
                         Model model) {

        String inputGiaTriGiam = request.getLoaiHinhGiam();

        // Kiểm tra lỗi chung và loại hình giảm giá rỗng
        if (inputGiaTriGiam == null || inputGiaTriGiam.trim().isEmpty() || bindingResult.hasErrors()) {
            model.addAttribute("message", "Vui lòng nhập đúng dữ liệu");
            model.addAttribute("request", request);
            return "admin/phieu-giam-gia/form-update";
        }

        // Kiểm tra loại hình giảm giá hợp lệ (phải là "phantram" hoặc "giatien")
        if (!inputGiaTriGiam.trim().equalsIgnoreCase("phantram") &&
                !inputGiaTriGiam.trim().equalsIgnoreCase("giatien")) {
            model.addAttribute("validLoaiHinhGiam", request);
            model.addAttribute("message", "Loại hình giảm phải là 'phantram' hoặc 'giatien'");
            return "admin/phieu-giam-gia/form-update";
        }

        // Kiểm tra ngày bắt đầu và ngày kết thúc
        LocalDateTime ngayBatDau = request.getNgayBatDau();
        LocalDateTime ngayKetThuc = request.getNgayKetThuc();
        LocalDateTime now = LocalDateTime.now();

        if (ngayBatDau == null || ngayKetThuc == null) {
            model.addAttribute("messages", "Ngày bắt đầu và ngày kết thúc không được để trống");
            model.addAttribute("message", "");
            return "admin/phieu-giam-gia/form-update";
        }

        if (ngayBatDau.isBefore(now)) {
            model.addAttribute("messages", "Ngày bắt đầu phải lớn hơn thời gian hiện tại");
            return "admin/phieu-giam-gia/form-update";
        }

        if (ngayBatDau.isAfter(ngayKetThuc)) {
            model.addAttribute("message", "");
            model.addAttribute("messages", "Ngày bắt đầu phải nhỏ hơn ngày kết thúc");
            return "admin/phieu-giam-gia/form-update";
        }

        // Nếu hợp lệ, tiến hành cập nhật dữ liệu
        String result = service.update(request);
        return "redirect:/admin/phieu-giam-gia/all";
    }

//delete
//    @PostMapping("/delete{id}")
//    public String delete(@Valid @ModelAttribute("request") UpdatePhieuGiamGiaRequest request,
//                         BindingResult bindingResult,
//                         Model model) {
//
//
//        return "redirect:/admin/phieu-giam-gia/all";
//    }

    @GetMapping("/form-detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model) {
        PhieuGiamGia phieuGiamGia = service.findById(id);
        model.addAttribute("phieuGiamGia", phieuGiamGia);
        return "admin/phieu-giam-gia/form-detail";
    }

    @GetMapping("/change-status/{id}")
    public String changeStatus(@PathVariable("id") Integer id) {
        service.changeStatus(id);
        return "redirect:/admin/phieu-giam-gia/all";
    }


}
