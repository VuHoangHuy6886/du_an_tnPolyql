package com.poliqlo.controllers.admin.dot_giam_gia.controller;

import com.poliqlo.controllers.admin.dot_giam_gia.model.request.AddDotGiamGiaRequest;
import com.poliqlo.controllers.admin.dot_giam_gia.model.request.EditDotGiamGiaRequest;
import com.poliqlo.controllers.admin.dot_giam_gia.service.DotGiamGiaService;
import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.SanPhamChiTiet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DotGiamGiaController {

    private final DotGiamGiaService service;

    @GetMapping("/admin/dot-giam-gia/all")
    public String showData(@RequestParam(value = "page", defaultValue = "0", required = false) String pageStr,
                           @RequestParam(value = "size", defaultValue = "5", required = false) String sizeStr,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "status", required = false) String status,
                           @RequestParam(value = "startTime", required = false) String startTimeStr,
                           @RequestParam(value = "endTime", required = false) String endTimeStr,
                           Model model) {
        int page, size;
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
            Page<DotGiamGia> pages = service.findAll(page, size);
            model.addAttribute("listProductIsDelete", service.findAllIsDeleteTrue());
            model.addAttribute("pages", pages);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pages.getTotalPages());
            return "admin/dot-giam-gia/index";
        }
        if (name.equals("")) {
            name = null;
        }
        if (status.equals("")) {
            status = null;
        }
        Page<DotGiamGia> pages = service.findAll(page, size, name, status, startTime, endTime);
        model.addAttribute("listProductIsDelete", service.findAllIsDeleteTrue());
        model.addAttribute("name", name);
        model.addAttribute("status", status);
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pages.getTotalPages());
        return "admin/dot-giam-gia/index";
    }


    @GetMapping("/admin/dot-giam-gia/form-add")
    public String formAdd(Model model) {
        model.addAttribute("DotGiamGiaNew", new AddDotGiamGiaRequest());
        model.addAttribute("products", service.productlist());
        return "admin/dot-giam-gia/form-add";
    }


    @PostMapping("/admin/dot-giam-gia/save")
    public String add(@Valid @ModelAttribute("DotGiamGiaNew") AddDotGiamGiaRequest request,
                      @RequestParam("productDetailIds") String ids,
                      BindingResult bindingResult,
                      Model model) {
        List<Integer> listIdProductDetail = Arrays.stream(ids.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Boolean check = service.existsByTen(request.getTen());
        if (check) {
            model.addAttribute("DotGiamGiaNew", request);
            model.addAttribute("message", "Tên đã tồn tại");
            return "admin/dot-giam-gia/form-add";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("DotGiamGiaNew", request);
            return "admin/dot-giam-gia/form-add";
        } else {
            service.add(request, listIdProductDetail);
            return "redirect:/admin/dot-giam-gia/all";
        }
    }

    @GetMapping("/admin/dot-giam-gia/form-update/{id}")
    public String formUpdate(@PathVariable("id") Integer id, Model model) {
        EditDotGiamGiaRequest request = service.findByIdForUpdate(id);
        model.addAttribute("DotGiamGiaUpdate", request);
        return "admin/dot-giam-gia/form-update";
    }

    @GetMapping("/admin/dot-giam-gia/view/{id}")
    public String viewForm(@PathVariable("id") Integer id, Model model) {
        EditDotGiamGiaRequest request = service.findByIdForUpdate(id);
        model.addAttribute("DotGiamGiaUpdate", request);
        return "admin/dot-giam-gia/view";
    }

    @PostMapping("/admin/dot-giam-gia/update")
    public String update(@Valid @ModelAttribute("DotGiamGiaUpdate") EditDotGiamGiaRequest request,
                         BindingResult bindingResult,
                         Model model) {
        Boolean check = service.existsByTenAndNotId(request.getTen(), request.getId());
        if (check) {
            model.addAttribute("DotGiamGiaUpdate", request);
            model.addAttribute("message", "Tên đã tồn tại");
            return "admin/dot-giam-gia/form-update";
        }

        if (!request.getLoaiChietKhau().trim().equals("Phần trăm") && !request.getLoaiChietKhau().trim().equals("Số tiền")) {
            model.addAttribute("DotGiamGiaUpdate", request);
            model.addAttribute("message", "Loại chiết khẩu phải là Phần trăm Hoặc Số tiền");
            return "admin/dot-giam-gia/form-update";
        }
        if (bindingResult.hasErrors()) {
            System.out.println("Có lỗi xảy ra:");
            bindingResult.getFieldErrors().forEach(error -> {
                System.out.println("Field: " + error.getField());
                System.out.println("Message: " + error.getDefaultMessage());
            });

            model.addAttribute("DotGiamGiaUpdate", request);
            return "admin/dot-giam-gia/form-update";
        } else {
            service.update(request);
            return "redirect:/admin/dot-giam-gia/all";
        }
    }

    @PostMapping("/admin/dot-giam-gia/listIdProducts")
    @ResponseBody
    public ResponseEntity<List<SanPhamChiTiet>> getProductDetailList(@RequestBody List<String> listIdProducts) {
        System.out.println("List Id Products: " + listIdProducts);
        try {
            List<Integer> listIdProductsInteger = listIdProducts.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            List<SanPhamChiTiet> sanPhamChiTiets = service.getProductDetailLists(listIdProductsInteger);
            return ResponseEntity.ok(sanPhamChiTiets);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi chuyển đổi dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admin/dot-giam-gia/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        System.out.println("đã vào controller xóa");
        service.deleteById(id);
        return "redirect:/admin/dot-giam-gia/all";
    }

    @GetMapping("/admin/dot-giam-gia/restore/{id}")
    public String restore(@PathVariable("id") Integer id) {
        service.restoreById(id);
        return "redirect:/admin/dot-giam-gia/all";
    }

    @GetMapping("/admin/dot-giam-gia/toggle-status/{id}")
    public String toggle(@PathVariable("id") Integer id) {
        service.toggleStatus(id);
        return "redirect:/admin/dot-giam-gia/all";
    }
}
