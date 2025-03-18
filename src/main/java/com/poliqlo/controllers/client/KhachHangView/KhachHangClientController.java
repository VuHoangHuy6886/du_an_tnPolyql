package com.poliqlo.controllers.client.KhachHangView;

import com.poliqlo.controllers.client.KhachHangView.dto.KhachHangResponseDTO;
import com.poliqlo.controllers.client.KhachHangView.mapper.KhachHangMapper;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.TaiKhoan;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class KhachHangClientController {
    private final KhachHangRepository khachHangRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    @GetMapping("/thong-tin-khach-hang")
    public String getThongTinTaiKhoan(Model model) {
        KhachHang khachHang = khachHangRepository.findById(1).get();
        KhachHangResponseDTO responseDTO = KhachHangMapper.KhachHangToResponse(khachHang);
        model.addAttribute("Response", responseDTO);
        return "/client/TaiKhoanKhachHang/index";
    }

    @PostMapping("/update")
    public String updateTaiKhoanKhachHang(@ModelAttribute("Response") KhachHangResponseDTO responseDTO, Model model) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(responseDTO.getIdTaiKhoan()).get();
        KhachHang khachHang = KhachHangMapper.responseToEntityCustomer(responseDTO,taiKhoan);
        taiKhoan.setSoDienThoai(responseDTO.getSoDienThoai());
        khachHangRepository.save(khachHang);
        taiKhoanRepository.save(taiKhoan);
        return "redirect:/thong-tin-khach-hang";
    }

}
