package com.poliqlo.controllers.admin.ban_hang.controller;


import com.poliqlo.controllers.admin.ban_hang.model.request.HoaDonDto;
import com.poliqlo.controllers.admin.ban_hang.model.response.KhachHangDto;
import com.poliqlo.controllers.admin.ban_hang.model.response.PhieuGiamGiaDto;
import com.poliqlo.controllers.admin.ban_hang.repository.impl.SanPhamRepositoryImpl;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.LichSuHoaDon;
import com.poliqlo.repositories.HoaDonRepository;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.PhieuGiamGiaRepository;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BanHangController {
    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;
    private final ModelMapper modelMapper;
    private final KhachHangRepository khachHangRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
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
        var khachHangPage = khachHangRepository.findKhachHangBySoDienThoai(q.orElse("%"), page);
        var resp = khachHangPage.map((element) -> modelMapper.map(element, KhachHangDto.class));
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/admin/api/v1/sale/customer/{id}")
    @ResponseBody
    public ResponseEntity<?> getKhachHang(
            @PathVariable(name = "id") KhachHang khachHang
    ) {
        var resp = modelMapper.map(khachHang, KhachHangDto.class);
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/admin/api/v1/sale/promotion")
    @ResponseBody
    public ResponseEntity<?> getPromotion(
            @PageableDefault() Pageable page,
            @RequestParam(name = "idKH", required = false) Integer khachHangId,
            @RequestParam(name = "price") Double price
    ) {
        Page<PhieuGiamGiaDto> phieuGiamGias = null;
        if (khachHangId == null) {
            phieuGiamGias = phieuGiamGiaRepository.findAllActive(price, page)
                    .map((element) -> modelMapper.map(element, PhieuGiamGiaDto.class));
        } else {
            phieuGiamGias = phieuGiamGiaRepository.findAllActive(khachHangId, price, page)
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
        var newHoaDon = modelMapper.map(req, com.poliqlo.models.HoaDon.class);
        var pgg = req.getPhieuGiamGiaId();
        if (pgg != null) {
            var newPggg = phieuGiamGiaRepository.findById(req.getPhieuGiamGiaId()).get();
            newPggg.setSoLuong(newPggg.getSoLuong() - 1);
            phieuGiamGiaRepository.save(newPggg);
        }
        newHoaDon.setNgayNhanHang(LocalDateTime.now());
        var lshd = new LichSuHoaDon();
        lshd.setMoTa("Hóa đơn mua hàng tại quầy");
        lshd.setTieuDe("Hóa đơn mua hàng tại quầy");
        lshd.setThoiGian(LocalDateTime.now());

        newHoaDon.setLichSuHoaDons(List.of(lshd));
        newHoaDon.getHoaDonChiTiets().forEach((element) -> {
            var newSPCT = sanPhamChiTietRepository.findById(element.getSanPhamChiTiet().getId()).get();
            newSPCT.setSoLuong(newSPCT.getSoLuong() - element.getSoLuong());
            if (newSPCT.getSoLuong() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số lượng sản phẩm trong kho không còn đủ : " + newSPCT.getSanPham().getTen() + " " + newSPCT.getMauSac().getTen() + " " + newSPCT.getKichThuoc().getTen() + " thiếu " + Math.abs(newSPCT.getSoLuong()) + " sản phẩm");
            }
            sanPhamChiTietRepository.save(newSPCT);
        });
        newHoaDon.setTrangThaiThanhToan("DA_THANH_TOAN");
        newHoaDon.setHoaDonChiTiets(newHoaDon.getHoaDonChiTiets()
                .stream()
                .peek(hdct -> hdct.setHoaDon(newHoaDon))
                .toList()
        );


        var resp = hoaDonRepository.save(newHoaDon);
        return ResponseEntity.ok(null);
    }
}
