package com.poliqlo.controllers.client.carts;

import com.poliqlo.controllers.client.carts.dto.CartDetailResponseDTO;
import com.poliqlo.controllers.client.carts.service.CartDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartClientController {
    private final CartDetailService service;

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<CartDetailResponseDTO> responseDTOList = service.getCartDetailByIdCustomer(1);
        model.addAttribute("carts", responseDTOList);
        return "client/cart";
    }
}
