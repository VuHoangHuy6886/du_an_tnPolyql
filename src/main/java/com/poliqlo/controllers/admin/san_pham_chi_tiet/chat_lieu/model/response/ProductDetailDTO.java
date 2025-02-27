package com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductDetailDTO {
    private Integer id;
    private String kichThuoc;
    private Double giaBan;
    private Integer soLuong;
    private String tenMau;
    private String anhUrlDetail;
}
