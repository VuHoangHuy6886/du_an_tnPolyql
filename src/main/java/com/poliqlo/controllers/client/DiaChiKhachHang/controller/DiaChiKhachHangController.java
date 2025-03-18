package com.poliqlo.controllers.client.DiaChiKhachHang.controller;

import com.poliqlo.controllers.client.DiaChiKhachHang.serviece.DiaChiKhachHangService;
import com.poliqlo.models.DiaChi;
import com.poliqlo.repositories.DiaChiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/dia-chi-khach-hang")
public class DiaChiKhachHangController {
    @Autowired
    private DiaChiKhachHangService diaChiKhachHangService;

    @GetMapping("/all")
    public String findAll(Model model) {
        model.addAttribute("diaChikhachHang", diaChiKhachHangService.getAllDiaChi());
        return "client/DiaChiKhachHang/index";
    }

    //chuyen trang them dia chi
    @GetMapping("/add")
    public String formAdd(Model model) {
        model.addAttribute("diaChikhachHang", new DiaChi());
        return "/client/DiaChiKhachHang/form-add";
    }

}
