package com.poliqlo.controllers.client.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailResponseDTO {
    private Integer id;
    private Integer customerId;
    private Integer productDetailId;
    private String name;
    private String color;
    private String size;
    private String price;
    private String quantity;
    private String totalPrice;
    private String url;
    private String priceAfterDiscount;
    private Integer discountId;
}
