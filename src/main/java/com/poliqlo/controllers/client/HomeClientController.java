package com.poliqlo.controllers.client;

import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.controllers.client.carts.service.CartDetailService;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
