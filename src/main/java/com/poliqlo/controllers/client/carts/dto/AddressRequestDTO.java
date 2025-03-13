package com.poliqlo.controllers.client.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressRequestDTO {
    private String provinceID;
    private String districtID;
    private String wardID;
    private String addressStr;
}
