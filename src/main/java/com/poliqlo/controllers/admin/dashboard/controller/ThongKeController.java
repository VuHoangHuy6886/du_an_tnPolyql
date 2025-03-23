package com.poliqlo.controllers.admin.dashboard.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ThongKeController {
    @GetMapping("/admin")
    public String index() {
        return "admin/dashboard/dashboard";
    }
}
