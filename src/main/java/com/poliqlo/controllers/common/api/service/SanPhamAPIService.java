package com.poliqlo.controllers.common.api.service;

import com.poliqlo.controllers.common.api.model.request.SanPhamSearchRequest;
import com.poliqlo.controllers.common.api.model.response.SanPhamAPIResponse;
import com.poliqlo.models.SanPham;
import com.poliqlo.repositories.SanPhamRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamAPIService {


    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> findAll(SanPhamSearchRequest request) {
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

           return modelMapper.map(sp, SanPhamAPIResponse.class);
        });



        return ResponseEntity.ok(resp);
    }
}
