package com.poliqlo.controllers.admin.gio_hang.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.poliqlo.models.NhanVien}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response implements Serializable {

    @NotNull
    private Integer idKhachHang;
    @NotNull
    private Integer idSanPhamChiTiet;
    @NotNull
    private Integer soLuong;

}