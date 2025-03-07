package com.poliqlo.controllers.common.api.service;

import com.poliqlo.controllers.common.api.model.request.SanPhamSearchRequest;
import com.poliqlo.controllers.common.api.model.response.SanPhamAPIResponse;
import com.poliqlo.controllers.common.api.model.response.SanPhamChiTietAPIResponse;
import com.poliqlo.models.SanPham;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamAPIService {


    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public Page<?> findAll(SanPhamSearchRequest request) {
        Specification<SanPham> specification=new Specification<SanPham>() {
            @Override
            public Predicate toPredicate(Root<SanPham> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction(); // Luôn đúng (true) để có thể thêm điều kiện động

                // Tìm theo danh sách ID
                if (request.getId() != null && !request.getId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("id").in(request.getId()));
                }

                // Tìm theo thương hiệu
                if (request.getThuongHieuId() != null && !request.getThuongHieuId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("thuongHieu").get("id").in(request.getThuongHieuId()));
                }

                // Tìm theo chất liệu
                if (request.getChatLieuId() != null && !request.getChatLieuId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("chatLieu").get("id").in(request.getChatLieuId()));
                }

                // Tìm theo kiểu dáng
                if (request.getKieuDangId() != null && !request.getKieuDangId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("kieuDang").get("id").in(request.getKieuDangId()));
                }

                // Tìm theo mã sản phẩm (LIKE)
                if (request.getMaSanPham() != null && !request.getMaSanPham().isEmpty()) {
                    Predicate orCondition = cb.disjunction();
                    for (String ma : request.getMaSanPham()) {
                        orCondition = cb.or(orCondition, cb.like(root.get("maSanPham"), "%" + ma + "%"));
                    }
                    predicate = cb.and(predicate, orCondition);
                }

                // Tìm theo tên sản phẩm (LIKE)
                if (request.getTen() != null && !request.getTen().isEmpty()) {
                    Predicate orCondition = cb.disjunction();
                    for (String ten : request.getTen()) {
                        orCondition = cb.or(orCondition, cb.like(root.get("ten"), "%" + ten + "%"));
                    }
                    predicate = cb.and(predicate, orCondition);
                }

                // Tìm theo trạng thái
                if (request.getTrangThai() != null && !request.getTrangThai().isEmpty()) {
                    predicate = cb.and(predicate, root.get("trangThai").in(request.getTrangThai()));
                }

                // Tìm theo danh mục
                if (request.getDanhMucIds() != null && !request.getDanhMucIds().isEmpty()) {
                    predicate = cb.and(predicate, root.join("danhMucs").get("id").in(request.getDanhMucIds()));
                }

                // Tìm theo kích thước
                if (request.getKichThuocId() != null && !request.getKichThuocId().isEmpty()) {
                    predicate = cb.and(predicate, root.join("sanPhamChiTiets").get("kichThuoc").get("id").in(request.getKichThuocId()));
                }



                // Tìm theo barcode
                if (request.getBarcode() != null && !request.getBarcode().isEmpty()) {
                    predicate = cb.and(predicate, root.join("sanPhamChiTiets").get("barcode").in(request.getBarcode()));
                }

                // Tìm theo màu sắc
                if (request.getMauSacId() != null && !request.getMauSacId().isEmpty()) {
                    predicate = cb.and(predicate, root.join("sanPhamChiTiets").get("mauSac").get("id").in(request.getMauSacId()));
                }

                // Lọc theo giá bán
                if (request.getMinPrice() != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.join("sanPhamChiTiets").get("giaBan"), request.getMinPrice()));
                }
                if (request.getMaxPrice() != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.join("sanPhamChiTiets").get("giaBan"), request.getMaxPrice()));
                }

                return predicate;
            }
        };
        Page<SanPham> pageResponse=null;
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getOrderBy());
        Pageable pageable = null;
        if(request.getPageSize()==null){
            List<SanPham> sanPhams=sanPhamRepository.findAll(specification, sort);
            pageable = Pageable.ofSize(sanPhams.size());
            pageResponse=new PageImpl<>(sanPhams,pageable,sanPhams.size());
        }else{
            pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
            pageResponse=sanPhamRepository.findAll(specification, pageable);
        }
        Page<SanPhamAPIResponse> resp=pageResponse.map(sp->{
           var sanPhamAPIResponse= modelMapper.map(sp, SanPhamAPIResponse.class);
           sanPhamAPIResponse.getSanPhamChiTiets().forEach(spct->{
               var dgg= spct.getDotGiamGias().stream()
                       .filter(p ->
                               p.getTrangThai().equalsIgnoreCase("HOAT_DONG") &&
                                       p.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                                       p.getThoiGianKetThuc().isAfter(LocalDateTime.now()) &&
                                       (!p.getIsDeleted())

                       ).max(Comparator.comparing(SanPhamAPIResponse.SanPhamChiTiet.DotGiamGiaDto::getId));
               spct.setDotGiamGia(dgg.orElse(null));
               spct.setIsPromotionProduct(spct.getDotGiamGia()==null);
           });
           return sanPhamAPIResponse;
        });



        return resp;
    }

    public ResponseEntity<?> findSPCTById(Integer id) {
        SanPhamChiTietAPIResponse sanPhamChiTietAPIResponse=null;
        var respModel=sanPhamChiTietRepository.findById(id);
        if(respModel.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Không tìm thấy sản phẩm chi tiết");
        }
        var resp=modelMapper.map(respModel.get(), SanPhamChiTietAPIResponse.class);
        var dgg= resp.getDotGiamGias().stream()
                .filter(p ->
                        p.getTrangThai().equalsIgnoreCase("HOAT_DONG") &&
                                p.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                                p.getThoiGianKetThuc().isAfter(LocalDateTime.now()) &&
                                (!p.getIsDeleted())

                ).max(Comparator.comparing(SanPhamChiTietAPIResponse.DotGiamGiaDto::getId));
        resp.setTen(resp.getSanPham().getTen()+" " +resp.getMauSac().getTen()+ " "+resp.getKichThuoc().getTen());
        resp.setDotGiamGia(dgg.orElse(null));
        resp.setGiaChietKhau(BigDecimal.valueOf(9999999999.99));
        return ResponseEntity.ok(resp);
    }
}
