package com.poliqlo.controllers.admin.san_pham.model.reponse;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProductDetailDTO {
    private Integer id;
    private BigDecimal giaBan;
    private Integer soLuong;
}