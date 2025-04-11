package com.poliqlo.controllers.admin.san_pham.model.reponse;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddProductDetailRequest {
    @NotNull(message = "ID sản phẩm không được để trống")
    private Integer sanPhamId;

    @NotNull(message = "Chọn kích thước")
    private Integer kichThuocId;

    @NotNull(message = "Chọn màu sắc")
    private Integer mauSacId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 0, message = "Giá bán không được nhỏ hơn 0")
    private BigDecimal giaBan;

    @Size(max = 100)
    private String barcode;

}