package com.poliqlo.controllers.client.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddressResponseDTO {
    private Integer id;
    private Integer provinceId;
    private Integer districtId;
    private String wardId;
    private String ten;
    private String soDienThoai;
    private Boolean defaultValue;
}
