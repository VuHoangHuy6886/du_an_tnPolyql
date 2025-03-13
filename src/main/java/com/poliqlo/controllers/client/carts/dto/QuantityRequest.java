package com.poliqlo.controllers.client.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantityRequest {
    private int id;
    private int quantity;
}
