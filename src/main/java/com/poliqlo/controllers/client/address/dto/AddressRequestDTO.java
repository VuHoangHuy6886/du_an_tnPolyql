package com.poliqlo.controllers.client.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressRequestDTO {
    private Integer customerID;
    private String provinceID;
    private String districtID;
    private String wardID;
    private String addressStr;
    private String tenNguoiNhan;
    private String phone;
}
