package com.poliqlo.controllers.admin.san_pham.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.poliqlo.models.ChatLieu}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Response implements Serializable {
    private Integer id;
    private String maSanPham;
    private String tenSanPham;
    private String thuongHieu;
    private String chatLieu;
    private String kieuDang;
    private String danhMuc;
//    private List<String> danhMuc;
    private String anhUrl;
    private Long soLuong;
    private String trangThai;

}