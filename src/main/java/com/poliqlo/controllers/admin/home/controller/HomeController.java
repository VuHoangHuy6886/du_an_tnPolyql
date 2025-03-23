package com.poliqlo.controllers.admin.home.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("")

public class HomeController {
    @GetMapping(path = {""," "})
    public String index() {
        return "admin/test";
    }
}
