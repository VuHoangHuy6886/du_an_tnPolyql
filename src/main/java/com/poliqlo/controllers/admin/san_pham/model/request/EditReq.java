package com.poliqlo.controllers.admin.san_pham.model.request;

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
public class EditReq implements Serializable {
    private Integer id;
    private String maSanPham;
    private String ten;
    private String trangThai;
    private Integer idThuongHieu;
    private Integer idChatLieu;
    private Integer idKieuDang;
    private String anhUrl;
}
