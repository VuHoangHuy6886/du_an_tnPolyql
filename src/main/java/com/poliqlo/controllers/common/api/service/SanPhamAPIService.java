package com.poliqlo.controllers.common.api.service;

import com.poliqlo.controllers.common.api.model.request.SanPhamSearchRequest;
import com.poliqlo.controllers.common.api.model.response.SanPhamAPIResponse;
import com.poliqlo.controllers.common.api.model.response.SanPhamChiTietAPIResponse;
import com.poliqlo.models.SanPham;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.SanPhamChiTietRepository;
import com.poliqlo.repositories.SanPhamRepository;
import jakarta.persistence.criteria.*;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SanPhamAPIService {


    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public Page<?> findAll(SanPhamSearchRequest request) {
        Specification<SanPham> specification = new Specification<SanPham>() {
            @Override
            public Predicate toPredicate(Root<SanPham> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();

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
                        orCondition = cb.or(orCondition, cb.like(cb.lower(root.get("ten")), "%" + ten.toLowerCase() + "%"));
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
                    Subquery<Integer> subquery = query.subquery(Integer.class);
                    Root<SanPhamChiTiet> subRoot = subquery.from(SanPhamChiTiet.class);

                    subquery.select(cb.literal(1))
                            .where(
                                    cb.equal(subRoot.get("sanPham"), root),
                                    subRoot.get("kichThuoc").get("id").in(request.getKichThuocId())
                            );

                    predicate = cb.and(predicate, cb.exists(subquery));
                }


                // Tìm theo barcode
                if (request.getBarcode() != null && !request.getBarcode().isEmpty()) {
                    predicate = cb.and(predicate, root.join("sanPhamChiTiets").get("barcode").in(request.getBarcode()));
                }

                // Tìm theo màu sắc
                if (request.getMauSacId() != null && !request.getMauSacId().isEmpty()) {
                    // Tạo subquery để tìm màu sắc của SanPham hiện tại
                    Subquery<Integer> subquery = query.subquery(Integer.class);
                    Root<SanPhamChiTiet> subRoot = subquery.from(SanPhamChiTiet.class);

                    subquery.select(cb.literal(1))
                            .where(
                                    cb.equal(subRoot.get("sanPham"), root),
                                    subRoot.get("mauSac").get("id").in(request.getMauSacId())
                            );

                    predicate = cb.and(predicate, cb.exists(subquery));
                }

                // Lọc theo giá bán
                if (request.getMinPrice() != null) {
                    Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
                    Root<SanPhamChiTiet> subRoot = subquery.from(SanPhamChiTiet.class);
                    subquery.select(cb.min(subRoot.get("giaBan")))
                            .where(cb.equal(subRoot.get("sanPham"), root));
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(subquery, request.getMinPrice()));
                }
                if (request.getMaxPrice() != null) {
                    Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
                    Root<SanPhamChiTiet> subRoot = subquery.from(SanPhamChiTiet.class);
                    subquery.select(cb.min(subRoot.get("giaBan")))
                            .where(cb.equal(subRoot.get("sanPham"), root));
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(subquery, request.getMaxPrice()));
                }

                return predicate;
            }
        };
        Page<SanPham> pageResponse = null;
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getOrderBy());
        Pageable pageable = null;
        List<SanPham> sanPhams = null;
        if (request.getPageSize() == null) {
            sanPhams = sanPhamRepository.findAll(specification, sort);
            pageable = Pageable.ofSize(sanPhams.size());
            pageResponse = new PageImpl<>(sanPhams, pageable, sanPhams.size());
        } else {
            pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
            pageResponse = sanPhamRepository.findAll(specification, pageable);
        }

        Page<SanPhamAPIResponse> resp = pageResponse.map(sp -> {

            var sanPhamAPIResponse = modelMapper.map(sp, SanPhamAPIResponse.class);
            sanPhamAPIResponse.getSanPhamChiTiets().forEach(spct -> {
                var dgg = spct.getDotGiamGias().stream()
                        .filter(p ->
                                p.getTrangThai().equalsIgnoreCase("DANG_DIEN_RA") &&
                                        p.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                                        p.getThoiGianKetThuc().isAfter(LocalDateTime.now()) &&
                                        (!p.getIsDeleted())

                        ).max(Comparator.comparing(SanPhamAPIResponse.SanPhamChiTiet.DotGiamGiaDto::getId));
                if (dgg.isPresent()) {
                    BigDecimal giaChietKhau = applyPromotion(spct.getGiaBan(), dgg.get().getLoaiChietKhau(), dgg.get().getGiaTriGiam(), dgg.get().getGiamToiDa());
                    spct.setGiaChietKhau(giaChietKhau);
                }else{
                    spct.setGiaChietKhau(spct.getGiaBan());
                }

                sanPhamAPIResponse.setIsPromotionProduct(dgg.isPresent());
                spct.setDotGiamGia(dgg.orElse(null));
                spct.setIsPromotionProduct(dgg.isPresent());

            });
            sanPhamAPIResponse.setSanPhamChiTiets(
                    sanPhamAPIResponse.getSanPhamChiTiets().stream()
                            .filter(
                    spct->
                                    !(spct.getIsDeleted()||spct.getSoLuong()<1)&&
                                    (request.getMinPrice()==null||spct.getGiaChietKhau().compareTo(request.getMinPrice())>=0)&&
                                    (request.getMaxPrice()==null||spct.getGiaChietKhau().compareTo(request.getMaxPrice())<=0)&&
                                    (request.getKichThuocId()==null|| !request.getKichThuocId().isEmpty() &&request.getKichThuocId().contains(spct.getKichThuoc().getId()))&&
                                    (request.getMauSacId()==null|| !request.getMauSacId().isEmpty() &&request.getMauSacId().contains(spct.getMauSac().getId()))
                            )
                            .collect(Collectors.toSet()));

            sanPhamAPIResponse.setSoLuong(sanPhamAPIResponse.getSanPhamChiTiets().stream().mapToInt(SanPhamAPIResponse.SanPhamChiTiet::getSoLuong).sum());
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
                        p.getTrangThai().equalsIgnoreCase("DANG_DIEN_RA") &&
                                p.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                                p.getThoiGianKetThuc().isAfter(LocalDateTime.now()) &&
                                (!p.getIsDeleted())

                ).max(Comparator.comparing(SanPhamChiTietAPIResponse.DotGiamGiaDto::getId));
        resp.setTen(resp.getSanPham().getTen()+" " +resp.getMauSac().getTen()+ " "+resp.getKichThuoc().getTen());
        resp.setDotGiamGia(dgg.orElse(null));
        if(dgg.isPresent()){
            BigDecimal giaChietKhau = applyPromotion(resp.getGiaBan(), dgg.get().getLoaiChietKhau(), dgg.get().getGiaTriGiam(), dgg.get().getGiamToiDa());
            resp.setGiaChietKhau(giaChietKhau);


        }else{
            resp.setGiaChietKhau(resp.getGiaBan());
        }
        resp.setIsPromotionProduct(dgg.isPresent());
        return ResponseEntity.ok(resp);
    }

    public Page<SanPhamChiTietAPIResponse> findAllSPCT(SanPhamSearchRequest request) {
        Specification<SanPhamChiTiet> specification=new Specification<SanPhamChiTiet>() {
            @Override
            public Predicate toPredicate(Root<SanPhamChiTiet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();

                // Tìm theo danh sách ID
                if (request.getId() != null && !request.getId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("id").in(request.getId()));
                }

                // Tìm theo thương hiệu
                if (request.getThuongHieuId() != null && !request.getThuongHieuId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("sanPham").get("thuongHieu").get("id").in(request.getThuongHieuId()));
                }

                // Tìm theo chất liệu
                if (request.getChatLieuId() != null && !request.getChatLieuId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("sanPham").get("chatLieu").get("id").in(request.getChatLieuId()));
                }

                // Tìm theo kiểu dáng
                if (request.getKieuDangId() != null && !request.getKieuDangId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("sanPham").get("kieuDang").get("id").in(request.getKieuDangId()));
                }

                // Tìm theo mã sản phẩm (LIKE)
                if (request.getMaSanPham() != null && !request.getMaSanPham().isEmpty()) {
                    Predicate orCondition = cb.disjunction();
                    for (String ma : request.getMaSanPham()) {
                        orCondition = cb.or(orCondition, cb.like(root.get("sanPham").get("maSanPham"), "%" + ma + "%"));
                    }
                    predicate = cb.and(predicate, orCondition);
                }

                // Tìm theo tên sản phẩm (LIKE)
                if (request.getTen() != null && !request.getTen().isEmpty()) {
                    Predicate orCondition = cb.disjunction();
                    for (String ten : request.getTen()) {
                        orCondition = cb.or(orCondition, cb.like(root.get("sanPham").get("ten"), "%" + ten + "%"));
                    }
                    predicate = cb.and(predicate, orCondition);
                }

                // Tìm theo trạng thái
                if (request.getTrangThai() != null && !request.getTrangThai().isEmpty()) {
                    predicate = cb.and(predicate, root.get("trangThai").in(request.getTrangThai()));
                }

                // Tìm theo danh mục
                if (request.getDanhMucIds() != null && !request.getDanhMucIds().isEmpty()) {
                    predicate = cb.and(predicate, root.join("sanPham").join("danhMucs").get("id").in(request.getDanhMucIds()));
                }

                // Tìm theo kích thước
                if (request.getKichThuocId() != null && !request.getKichThuocId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("kichThuoc").get("id").in(request.getKichThuocId()));
                }



                // Tìm theo barcode
                if (request.getBarcode() != null && !request.getBarcode().isEmpty()) {
                    predicate = cb.and(predicate, root.get("barcode").in(request.getBarcode()));
                }

                // Tìm theo màu sắc
                if (request.getMauSacId() != null && !request.getMauSacId().isEmpty()) {
                    predicate = cb.and(predicate, root.get("mauSac").get("id").in(request.getMauSacId()));
                }

                // Lọc theo giá bán
                if (request.getMinPrice() != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("giaBan"), request.getMinPrice()));
                }
                if (request.getMaxPrice() != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("giaBan"), request.getMaxPrice()));
                }

                return predicate;
            }
        };
        Page<SanPhamChiTiet> pageResponse=null;
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getOrderBy());
        Pageable pageable = null;
        if(request.getPageSize()==null){
            List<SanPhamChiTiet> sanPhamChiTiets=sanPhamChiTietRepository.findAll(specification, sort);
            pageable = Pageable.ofSize(sanPhamChiTiets.size());
            pageResponse=new PageImpl<>(sanPhamChiTiets,pageable,sanPhamChiTiets.size());
        }else{
            pageable = PageRequest.of(request.getPage(), request.getPageSize(), sort);
            pageResponse=sanPhamChiTietRepository.findAll(specification, pageable);
        }
        Page<SanPhamChiTietAPIResponse> resp=pageResponse.map(spct->{
            var sanPhamAPIResponse= modelMapper.map(spct, SanPhamChiTietAPIResponse.class);

                var dgg= sanPhamAPIResponse.getDotGiamGias().stream()
                        .filter(p ->
                                p.getTrangThai().equalsIgnoreCase("DANG_DIEN_RA") &&
                                        p.getThoiGianBatDau().isBefore(LocalDateTime.now()) &&
                                        p.getThoiGianKetThuc().isAfter(LocalDateTime.now()) &&
                                        (!p.getIsDeleted())

                        ).max(Comparator.comparing(SanPhamChiTietAPIResponse.DotGiamGiaDto::getId));
            sanPhamAPIResponse.setDotGiamGia(dgg.orElse(null));
            if(dgg.isPresent()){
                BigDecimal giaChietKhau=applyPromotion(spct.getGiaBan(),dgg.get().getLoaiChietKhau(),dgg.get().getGiaTriGiam(),dgg.get().getGiamToiDa());
                sanPhamAPIResponse.setGiaChietKhau(giaChietKhau);
            }else{
                sanPhamAPIResponse.setGiaChietKhau(sanPhamAPIResponse.getGiaBan());
            }


            sanPhamAPIResponse.setIsPromotionProduct(dgg.isPresent());
            return sanPhamAPIResponse;
        });
        return resp;

    }
    private BigDecimal applyPromotion(BigDecimal price, String type, BigDecimal value, BigDecimal max){
        Long giaBan=price.longValue();
        Long giaTriGiam=value.longValue();
        Long giamToiDa=max.longValue();
        Long promotionValue=0L;
        promotionValue=switch (type) {
            case "PHAN_TRAM" ->giaBan*giaTriGiam/100>giamToiDa?giamToiDa:giaBan*giaTriGiam/100;
            case "SO_TIEN" -> giaTriGiam;
            default -> 0L;
        };
        return BigDecimal.valueOf(giaBan-promotionValue>0?giaBan-promotionValue: 0L);
    }
}
