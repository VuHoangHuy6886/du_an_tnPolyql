package com.poliqlo.controllers.admin.gio_hang.model.request;

import com.poliqlo.models.KhachHang;
import com.poliqlo.models.SanPhamChiTiet;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.poliqlo.models.NhanVien}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EditReq implements Serializable {
    @NotNull
    @Positive
    Integer id;
    @NotNull
    private Integer idKhachHang;
    @NotNull
    private Integer idSanPhamChiTiet;
    @NotNull
    private Integer soLuong;
    @NotNull
    private Instant ngayThemVao;

}