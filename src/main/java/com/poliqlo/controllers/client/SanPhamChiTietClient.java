package com.poliqlo.controllers.client;

import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
@RequiredArgsConstructor
public class SanPhamChiTietClient {
    private final ThuongHieuRepository thuongHieuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final KieuDangRepository kieuDangRepository;
    private final DanhMucRepository danhMucRepository;
    private final MauSacRepository mauSacRepository;
    private final KichThuocRepository kichThuocRepository;
    private final SanPhamRepository sanPhamRepository;

    @GetMapping("/san-pham/{id}")
    public String demoClient2(
            @PathVariable("id") int id, // Lấy id từ URL
            Model model,
            HttpSession httpSession) {
        var relesProductSession=httpSession.getAttribute("listReliesProduct");
        Set<Integer> listRelesProduct=new LinkedHashSet<>();
        if(relesProductSession!=null){
            listRelesProduct = (Set<Integer>) relesProductSession;
        }
        listRelesProduct.add(id);


        httpSession.setAttribute("listReliesProduct", listRelesProduct);
        var resp=sanPhamRepository.findAllById(listRelesProduct);
        Map<Integer, Integer> idIndexMap = new HashMap<>();
        AtomicInteger i=new AtomicInteger(0);
        listRelesProduct.forEach(e->{
            idIndexMap.put(e,i.getAndIncrement());
        });


        resp.sort(Comparator.comparingInt(sp->-idIndexMap.get(sp.getId())));
        model.addAttribute("listReliesProduct", resp);


        return "client/trang-san-pham-chi-tiet";
    }
    @GetMapping("/san-pham")
    public String demoClient4(Model model) {
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.findAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.findAll();
        List<DanhMuc> listDanhMuc = danhMucRepository.findAll();
        List<KieuDang> listKieuDang = kieuDangRepository.findAll();
        List<MauSac> listMauSac = mauSacRepository.findAll();
        List<KichThuoc> listKichThuoc = kichThuocRepository.findAll();
        model.addAttribute("listThuongHieu", listThuongHieu);
        model.addAttribute("listChatLieu", listChatLieu);
        model.addAttribute("listDanhMuc", listDanhMuc);
        model.addAttribute("listKieuDang", listKieuDang);
        model.addAttribute("listMauSac", listMauSac);
        model.addAttribute("listKichThuoc", listKichThuoc);
        return "client/trang-san-pham";
    }
}
