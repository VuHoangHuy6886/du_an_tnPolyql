package com.poliqlo.controllers.admin.ban_hang.controller;


import com.poliqlo.controllers.admin.ban_hang.model.request.HoaDonDto;
import com.poliqlo.controllers.admin.ban_hang.model.response.KhachHangDto;
import com.poliqlo.controllers.admin.ban_hang.model.response.PhieuGiamGiaDto;
import com.poliqlo.controllers.admin.ban_hang.repository.impl.SanPhamRepositoryImpl;
import com.poliqlo.models.KhachHang;
import com.poliqlo.repositories.HoaDonRepository;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.PhieuGiamGiaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BanHangController {
    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;
    private final ModelMapper modelMapper;
    private final KhachHangRepository khachHangRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final HoaDonRepository hoaDonRepository;
//    private final MaGiamGiaRepository maGiamGiaRepository;
//    private final ImeiRepository imeiRepository;
//    private final SanPhamChiTietRepository sanPhamChiTietRepository;
//    private final KhachHangRepository khachHangRepository;
//    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;
//    private final IBanHangService banHangService;
//
//    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
//    private final ModelMapper modelMapper;
//    private final SanPhamRepository sanPhamRepository;
//    private final ImageService imageService;
//    private final AnhRepository anhRepository;

    @GetMapping("/admin/sale")
    public String sale(Model model) {
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "admin/ban-hang/ban-hang";
    }
    @GetMapping("/admin/api/v1/sale/khach-hang")
    @ResponseBody
    public ResponseEntity<Page<KhachHangDto>> getKhachHang(
            @RequestParam Optional<String> q,
            @PageableDefault() Pageable page
            ) {
        var khachHangPage=khachHangRepository.findKhachHangBySoDienThoai(q.orElse("%"),page);
        var resp= khachHangPage.map((element) -> modelMapper.map(element, KhachHangDto.class));
        return ResponseEntity.ok(resp);
    }
    @GetMapping("/admin/api/v1/sale/customer/{id}")
    @ResponseBody
    public ResponseEntity<?> getKhachHang(
            @PathVariable(name="id") KhachHang khachHang
            ) {
        var resp= modelMapper.map(khachHang, KhachHangDto.class);
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/admin/api/v1/sale/promotion")
    @ResponseBody
    public ResponseEntity<?> getPromotion(
            @PageableDefault() Pageable page,
            @RequestParam(name = "idKH", required = false)Integer khachHangId,
            @RequestParam(name="price") Double price
    ) {
        Page<PhieuGiamGiaDto> phieuGiamGias=null;
        if (khachHangId == null) {
            phieuGiamGias=phieuGiamGiaRepository.findAllActive(price,page)
                    .map((element) -> modelMapper.map(element, PhieuGiamGiaDto.class));
        }else{
            phieuGiamGias = phieuGiamGiaRepository.findAllActive(khachHangId,price,page)
                    .map((element) -> modelMapper.map(element, PhieuGiamGiaDto.class));
        }

        return ResponseEntity.ok(phieuGiamGias);
    }

    @PutMapping("/admin/api/v1/sale")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> persist(
            @RequestBody HoaDonDto req
    ) {
        var newHoaDon=modelMapper.map(req, com.poliqlo.models.HoaDon.class);
        var pgg=req.getPhieuGiamGiaId();
        if(pgg!=null){
           var newPggg=hoaDonRepository.findById(req.getPhieuGiamGiaId()).get().getPhieuGiamGia();
            newPggg.setSoLuong(newPggg.getSoLuong()-1);
            newHoaDon.setPhieuGiamGia(newPggg);
        }
        req.getHoaDonChiTiets().stream().map((element) -> modelMapper.map(element, com.poliqlo.models.HoaDonChiTiet.class)).forEach(newHoaDon.getHoaDonChiTiets()::add);

//        hoaDonRepository.save(newHoaDon);
        return ResponseEntity.ok(newHoaDon);
    }
}
