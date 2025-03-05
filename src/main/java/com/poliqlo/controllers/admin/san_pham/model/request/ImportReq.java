package com.poliqlo.controllers.admin.san_pham.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ImportReq implements Serializable {

    private Integer id;

    @NotNull(message = "Tên không được để trống")
    @Size(max = 255, message = "Tên không được vượt quá 255 ký tự")
    @NotBlank(message = "Tên không được để trống")
    private String ten;

    @NotNull(message = "ID thương hiệu không được để trống")
    private Integer idThuongHieu;

    @NotNull(message = "ID chất liệu không được để trống")
    private Integer idChatLieu;

    @NotNull(message = "ID kiểu dáng không được để trống")
    private Integer idKieuDang;

    @NotNull(message = "Mã sản phẩm không được để trống")
    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(max = 255, message = "Mã sản phẩm không được vượt quá 255 ký tự")
    private String maSanPham;

    @NotNull(message = "Trạng thái không được để trống")
    private String trangThai;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String moTa;

    @Size(max = 255, message = "Đường dẫn ảnh không được vượt quá 255 ký tự")
    private String anhUrl;

    private Boolean isDeleted;
}
