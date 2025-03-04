package com.poliqlo.controllers.admin.san_pham_chi_tiet.kieu_dang.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.poliqlo.models.KieuDang}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ImportReq implements Serializable {
    Integer id;
    @NotNull(message = "Tên không được trống")
    @Size(max = 255)
    @NotBlank(message = "Tên không được trống")
    private String ten;
    private Boolean isDeleted;
}