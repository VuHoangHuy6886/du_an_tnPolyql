package com.poliqlo.controllers.client.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillRequestDTO {
    String customerId;
    String voucherId;
    String cartDetailIds;
    String totalPrice;
    String addressId;
    String shipping;
    String paymentMethod;
    String billType;
    String coupon;
    String note;
}
