
package com.poliqlo.controllers.admin.chi_tiet_san_pham.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for adding a detailed product (ChiTietSanPham)
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddRequest implements Serializable {

    @NotNull(message = "ID sản phẩm không được để trống")
    private Long idSanPham;

    @NotNull(message = "ID kích thước không được để trống")
    private Long idKichThuoc;

    @NotNull(message = "ID màu sắc không được để trống")
    private Long idMauSac;

    private Long idDotGiamGia; // Giảm giá có thể không bắt buộc

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 0, message = "Giá bán phải lớn hơn hoặc bằng 0")
    private BigDecimal giaBan;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer soLuong;

    @Size(max = 255, message = "Barcode không được vượt quá 255 ký tự")
    private String barcode; // Barcode có thể null

    private Boolean isDeleted = false;
}
