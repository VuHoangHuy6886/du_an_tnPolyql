package com.poliqlo.controllers.admin.gio_hang.service;

import com.poliqlo.controllers.admin.gio_hang.model.response.Response;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.controllers.common.file.service.BlobStoreService;
import com.poliqlo.models.GioHangChiTiet;
import com.poliqlo.models.KhachHang;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.GioHangChiTietRepository;
import com.poliqlo.repositories.KhachHangRepository;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.TaiKhoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public GioHangChiTiet addToCart(Response request) {
        // 1. Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangRepository.findById(
                authService.getCurrentUserDetails().get().getKhachHang().getId()
        ).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // 2. Lấy sản phẩm chi tiết (SanPhamChiTiet)
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

        // 3. Kiểm tra xem khách hàng đã có chi tiết giỏ hàng với sản phẩm này chưa
        Optional<GioHangChiTiet> optionalCartDetail = gioHangChiTietRepository
                .findByKhachHangAndSanPhamChiTietAndIsDeletedFalse(khachHang, sanPhamChiTiet);

        // 4. Tính số lượng cần thêm
        int soLuongCanThem = request.getSoLuong();

        if (optionalCartDetail.isPresent()) {
            // 4a. Nếu đã có trong giỏ, cộng dồn số lượng
            GioHangChiTiet existing = optionalCartDetail.get();
            int newQuantity = existing.getSoLuong() + soLuongCanThem;

            // 4b. Kiểm tra tồn kho: Nếu newQuantity > sanPhamChiTiet.getSoLuong(), ném lỗi
            if (newQuantity > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Không đủ tồn kho. Tồn kho hiện tại: " + sanPhamChiTiet.getSoLuong());
            }

            // 4c. Cập nhật số lượng
            existing.setSoLuong(newQuantity);
            return gioHangChiTietRepository.save(existing);
        } else {
            // 5a. Nếu chưa có, kiểm tra tồn kho trực tiếp với soLuongCanThem
            if (soLuongCanThem > sanPhamChiTiet.getSoLuong()) {
                throw new RuntimeException("Không đủ tồn kho. Tồn kho hiện tại: " + sanPhamChiTiet.getSoLuong());
            }

            // 5b. Tạo mới đối tượng giỏ hàng chi tiết
            GioHangChiTiet newCartDetail = GioHangChiTiet.builder()
                    .khachHang(khachHang)
                    .sanPhamChiTiet(sanPhamChiTiet)
                    .soLuong(soLuongCanThem)
                    .ngayThemVao(Instant.now())
                    .isDeleted(false)
                    .build();

            return gioHangChiTietRepository.save(newCartDetail);
        }
    }



    public List<GioHangChiTiet> getAllGioHangChiTiet() {
        return gioHangChiTietRepository.findAll();
    }
    public GioHangChiTiet findById(Integer id) {
        return gioHangChiTietRepository.findById(id).orElse(null);
    }





}
