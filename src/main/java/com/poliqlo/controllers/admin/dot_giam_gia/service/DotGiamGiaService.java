package com.poliqlo.controllers.admin.dot_giam_gia.service;

import com.poliqlo.controllers.admin.dot_giam_gia.Mapper.DotGiamGiaMapper;
import com.poliqlo.controllers.admin.dot_giam_gia.model.request.AddDotGiamGiaRequest;
import com.poliqlo.controllers.admin.dot_giam_gia.model.request.EditDotGiamGiaRequest;
import com.poliqlo.models.DotGiamGia;
import com.poliqlo.models.SanPham;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.models.SanPhamChiTietDotGiamGia;
import com.poliqlo.repositories.DotGiamGiaRepository;
import com.poliqlo.repositories.SanPhamChiTietDotGiamGiaRepository;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import com.poliqlo.utils.DiscountStatusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DotGiamGiaService {
    private final DotGiamGiaMapper mapper;
    private final DotGiamGiaRepository dotGiamGiaRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final SanPhamChiTietDotGiamGiaRepository sanPhamChiTietDotGiamGiaRepository;

    public Page<DotGiamGia> findAll(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<DotGiamGia> dotGiamGiaPage = dotGiamGiaRepository.findAll(pageable);
        return dotGiamGiaPage;
    }

    public Page<DotGiamGia> findAll(Integer pageNo, Integer pageSize, String tenOrMa, String trangThai, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<DotGiamGia> dotGiamGiaPage = dotGiamGiaRepository.filterAllDiscount(pageable, tenOrMa, trangThai, thoiGianBatDau, thoiGianKetThuc);
        return dotGiamGiaPage;
    }

    public String add(AddDotGiamGiaRequest request, List<Integer> ids) {
        DotGiamGia dotGiamGia = mapper.toDotGiamGia(request, genMa());
        DotGiamGia result = dotGiamGiaRepository.save(dotGiamGia);
        // apply to product detail
        for (int i = 0; i < ids.toArray().length; i++) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(ids.get(i)).get();
            SanPhamChiTietDotGiamGia discountToProduct = SanPhamChiTietDotGiamGia.builder()
                    .dotGiamGia(result)
                    .sanPhamChiTiet(sanPhamChiTiet)
                    .soLuong(50)
                    .build();
            sanPhamChiTietDotGiamGiaRepository.save(discountToProduct);
        }
        return "Add Đợt Giảm Giá Thành Công";
    }

    public DotGiamGia update(EditDotGiamGiaRequest request) {
        DotGiamGia dotGiamGia = mapper.EditToDotGiamGia(request);
        return dotGiamGiaRepository.save(dotGiamGia);
    }

    public DotGiamGia findById(Integer id) {
        return dotGiamGiaRepository.findById(id).get();
    }

    public EditDotGiamGiaRequest findByIdForUpdate(Integer id) {
        DotGiamGia dotGiamGia = dotGiamGiaRepository.findById(id).get();
        EditDotGiamGiaRequest request = mapper.dotGiamGiaToEditDotGiamGiaRequest(dotGiamGia);
        return request;
    }

    public Page<SanPham> findAllProduct(Integer pageNo, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return sanPhamRepository.findAll(pageable);
    }

    public Page<SanPham> findAllPages(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return sanPhamRepository.findAll(pageable);
    }

    public boolean existsByTen(String name) {
        return dotGiamGiaRepository.existsByTen(name);
    }

    public boolean existsByTenAndNotId(String name, Integer id) {
        return dotGiamGiaRepository.existsByTenAndNotId(name, id);
    }

    public Page<SanPham> hienThiSanPhamPages(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SanPham> productPage = sanPhamRepository.findAll(pageable);
        return productPage;
    }

    public List<SanPham> productlist() {
        return sanPhamRepository.findAll();
    }

    public List<SanPhamChiTiet> getProductDetailLists(List<Integer> idProductList) {
        return sanPhamChiTietRepository.findByListByIdProductsForDiscounts(idProductList);
    }

    public String genMa() {
        return dotGiamGiaRepository.generateMaDotGiamGia();
    }

    // function update status discount by scheduler
    public void updateStatusDiscount(List<DotGiamGia> dotGiamGiaList) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Định dạng thời gian để hiển thị
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        String formattedTime = timeNow.format(formatter);
        System.out.println("Thời gian hiện tại: " + formattedTime);

        // Duyệt qua từng đợt giảm giá
        for (DotGiamGia dgg : dotGiamGiaList) {
            LocalDateTime startTime = dgg.getThoiGianBatDau();
            LocalDateTime endTime = dgg.getThoiGianKetThuc();

            System.out.println("\nID đợt giảm giá: " + dgg.getId());
            System.out.println("Thời gian bắt đầu: " + startTime.format(formatter));
            System.out.println("Thời gian kết thúc: " + endTime.format(formatter));

            // set trạng thái


            if (timeNow.isBefore(startTime)) {
                // check trạng thái
                String status = DiscountStatusUtil.getStatus(startTime, endTime, false);
                if (!status.equals(dgg.getTrangThai())) {
                    dgg.setTrangThai(DiscountStatusUtil.getStatus(startTime, endTime, false));
                    dotGiamGiaRepository.save(dgg);
                } else {
                    System.out.println("sắp diễn ra ko có j thay đổi");
                }
            } else if (timeNow.isAfter(endTime)) {
                // check trạng thái
                String status = DiscountStatusUtil.getStatus(startTime, endTime, false);
                if (!status.equals(dgg.getTrangThai())) {
                    dgg.setTrangThai(DiscountStatusUtil.getStatus(startTime, endTime, false));
                    dotGiamGiaRepository.save(dgg);
                } else {
                    System.out.println("đã kết thúc ra ko có j thay đổi");
                }
            } else {
                // check trạng thái
                String status = DiscountStatusUtil.getStatus(startTime, endTime, false);
                if (!status.equals(dgg.getTrangThai())) {
                    dgg.setTrangThai(DiscountStatusUtil.getStatus(startTime, endTime, false));
                    dotGiamGiaRepository.save(dgg);
                } else {
                    System.out.println(" đang diễn ra ko có j thay đổi");
                }
            }
        }
    }

    // update status
    public void updateStatus(Integer id) {
        DotGiamGia dotGiamGia = findById(id);
        dotGiamGia.setTrangThai(DiscountStatusUtil.HUY_BO);
        dotGiamGiaRepository.save(dotGiamGia);
    }

    // xoa
    public void deleteById(Integer id) {
        DotGiamGia dotGiamGia = findById(id);
        dotGiamGia.setIsDeleted(true);
        dotGiamGiaRepository.save(dotGiamGia);
    }

    // khôi phục
    public void restoreById(Integer id) {
        DotGiamGia dotGiamGia = findById(id);
        dotGiamGia.setIsDeleted(false);
        dotGiamGiaRepository.save(dotGiamGia);
    }


    public List<DotGiamGia> listDiscountsUpdateStatus() {
        return dotGiamGiaRepository.findAll();
    }
}
