package com.poliqlo.controllers.client;

import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class HomeClientController {


    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;


    @GetMapping("/")
    public String demoClient2(Model model) {

        return "client/client";
    }




}
