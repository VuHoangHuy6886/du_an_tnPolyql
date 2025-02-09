package com.poliqlo.controllers.admin.home.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")

public class HomeController {
    @GetMapping("")
    public String index() {
        return "redirect:/admin/san-pham-chi-tiet/danh-muc";
    }
}
