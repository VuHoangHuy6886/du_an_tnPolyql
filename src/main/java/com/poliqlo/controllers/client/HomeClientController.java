package com.poliqlo.controllers.client;

import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeClientController {


    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/")
    public String demoClient2(Model model) {
        return "redirect:/san-pham";
    }
}
