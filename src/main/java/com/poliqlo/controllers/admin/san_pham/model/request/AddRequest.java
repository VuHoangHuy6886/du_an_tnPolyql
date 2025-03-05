package com.poliqlo.controllers.admin.san_pham.model.request;

import com.poliqlo.models.SanPham;
import com.poliqlo.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddRequest implements Serializable {
    @NotNull
    @Positive
    private Integer id;

    @NotNull(message = "Tên không được trống")
    @Size(max = 255, message = "Độ dài vượt quá 255 ký tự")
    @NotBlank(message = "Tên không được trống")
    private String ten;
    final Boolean isDeleted = false;

    @NotNull(message = "ID thương hiệu không được để trống")
    private Integer idThuongHieu;

    @NotNull(message = "ID chất liệu không được để trống")
    private Integer idChatLieu;

    @NotNull(message = "ID kiểu dáng không được để trống")
    private Integer idKieuDang;

    @NotNull(message = "Mã sản phẩm không được để trống")
    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(max = 255, message = "Độ dài mã sản phẩm vượt quá 255 ký tự")
    @Unique(message = "Mã sản phẩm đã tồn tại", fieldName = "maSanPham", entityClass = SanPham.class)
    private String maSanPham;


    @Size(max = 1000, message = "Mô tả vượt quá 1000 ký tự")
    private String moTa;

    @Size(max = 255, message = "Đường dẫn ảnh vượt quá 255 ký tự")
    private String anhUrl;
}
