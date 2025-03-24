package com.poliqlo.controllers.admin.gio_hang.service;

import com.poliqlo.controllers.admin.gio_hang.model.response.Response;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.GioHangChiTiet;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.GioHangChiTietRepository;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GioHangService {
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private BlobStoreService blobStoreService;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private AuthService authService;

    public void saveGioHangChiTiet(GioHangChiTiet gioHang) {
        gioHangChiTietRepository.save(gioHang);
    }

    public GioHangChiTiet addToCart(Response request) {
        KhachHang khachHang = khachHangRepository.findById(authService.getCurrentUserDetails().get().getKhachHang().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        GioHangChiTiet gioHangChiTiet = GioHangChiTiet.builder()
                .khachHang(khachHang)
                .sanPhamChiTiet(sanPhamChiTiet)
                .soLuong(request.getSoLuong())
                .ngayThemVao(Instant.now())
                .isDeleted(false)
                .build();

        return gioHangChiTietRepository.save(gioHangChiTiet);
    }

    public List<GioHangChiTiet> getAllGioHangChiTiet() {
        return gioHangChiTietRepository.findAll();
    }

    public GioHangChiTiet findById(Integer id) {
        return gioHangChiTietRepository.findById(id).orElse(null);
    }
}
