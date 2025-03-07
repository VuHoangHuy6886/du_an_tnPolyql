package com.poliqlo.controllers.client.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailRequestDTO {
    private String customerId;
    private String productId;
    private String name;
    private String color;
    private String size;
    private String price;
    private String quantity;
    private String totalPrice;
    private String priceAfterDiscount;
}
