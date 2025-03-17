package com.poliqlo.controllers.client;
import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SanPhamChiTietClient {
    private final ThuongHieuRepository thuongHieuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final KieuDangRepository kieuDangRepository;
    private final DanhMucRepository danhMucRepository;
    private final MauSacRepository mauSacRepository;
    @GetMapping("/san-pham/{id}")
    public String demoClient2(
            @PathVariable("id") int id, // Lấy id từ URL
            Model model
    ) {


        return "client/trang-san-pham-chi-tiet";
    }
    @GetMapping("/san-pham")
    public String demoClient4(Model model) {
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.findAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.findAll();
        List<DanhMuc> listDanhMuc = danhMucRepository.findAll();
        List<KieuDang> listKieuDang = kieuDangRepository.findAll();
        List<MauSac> listMauSac = mauSacRepository.findAll();
        model.addAttribute("listThuongHieu", listThuongHieu);
        model.addAttribute("listChatLieu", listChatLieu);
        model.addAttribute("listDanhMuc", listDanhMuc);
        model.addAttribute("listKieuDang", listKieuDang);
        model.addAttribute("listMauSac", listMauSac);
        return "client/trang-san-pham";
    }
}
