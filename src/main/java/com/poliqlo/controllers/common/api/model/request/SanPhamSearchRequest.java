package com.poliqlo.controllers.common.api.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.poliqlo.models.SanPham}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamSearchRequest {
    private List<Integer> id;
    private List<Integer> thuongHieuId;
    private List<Integer> chatLieuId;
    private List<Integer> kieuDangId;
    private List<String> maSanPham;
    private List<String> ten;
    private List<String> trangThai;
    private List<Integer> danhMucIds;
    private List<Integer> kichThuocId;
    private List<String> barcode;
    private List<Integer> mauSacId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer page=0;
    private Integer pageSize=1000;
    private String orderBy = "id";
    private String sortDirection = "asc";
}

