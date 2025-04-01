package com.poliqlo.controllers.common.auth.controller;

import com.poliqlo.controllers.common.auth.model.request.SignInRequest;
import com.poliqlo.controllers.common.auth.model.request.SignUpRequest;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.TaiKhoan;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {
    private final HttpSession httpSession;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/sign-in")
    public String signin(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("signin_req", new SignInRequest());
        if (error != null) {
            switch (error) {
                case "401":{
                    model.addAttribute("message", "Bạn cần đăng nhập để truy cập trang này");
                    break;
                }
                case "404":{
                    model.addAttribute("message", "Tài khoản chưa tồn tại bạn cần đăng ký");
                    break;
                }
            }
        }
        return "authentication/authentication";
    }
    @GetMapping("/sign-up")
    public String signin(Model model) {
        model.addAttribute("signin_req", new SignInRequest());
        return "authentication/authentication";
    }



//    @ResponseBody
//    @PostMapping("/sign-in")
//    public ResponseEntity<JwtAuthenticationResponse> signin(
//            @RequestBody SignInRequest signInRequest,
//            @Value("${jwt.expiration}") Integer expiration,
//            HttpServletResponse response) {
//
//
//        JwtAuthenticationResponse token = null;
//        try {
//            token = authenticationService.signin(signInRequest.getEmail(), signInRequest.getPassword());
//            Cookie cookie = new Cookie("Authorization", token.getToken());
////        cookie.setSecure(true); // Đảm bảo cookie chỉ được gửi qua HTTPS
//            cookie.setPath("/");
//            cookie.setMaxAge(expiration); // 10 giờ
//            response.addCookie(cookie);
//            return ResponseEntity.ok(token);
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.notFound().build();
//        }
//
//    }


    @GetMapping("/home")
    public String home() {

        try {
            String successUrl = (String) httpSession.getAttribute("successUrl");
            httpSession.removeAttribute("successUrl");
            if (successUrl!=null&&!successUrl.isEmpty()){
                return "redirect:"+successUrl;
            }
            TaiKhoan taiKhoan = (TaiKhoan) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return switch (taiKhoan.getRole()) {
                case "ROLE_ADMIN" -> "redirect:/admin";
                case "ROLE_EMPLOYEE" -> "redirect:/admin/sale";
                default -> "redirect:/";
            };
        } catch (Exception e) {
            return "redirect:/";
        }

    }
    @ResponseBody
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest request,BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        var taiKhoan=new TaiKhoan();
        var khachHang=new KhachHang();
        khachHang.setTen(request.getTen());
        taiKhoan.setSoDienThoai(request.getSoDienThoai());
        taiKhoan.setPassword(passwordEncoder.encode(request.getMatKhau()));
        taiKhoan.setEmail(request.getEmail());
        taiKhoan.setRole(TaiKhoan.Role.ROLE_USER);
        taiKhoan.setKhachHang(khachHang);
        httpSession.setAttribute("newTaiKhoan",taiKhoan);
        return ResponseEntity.ok().build();
    }


}
