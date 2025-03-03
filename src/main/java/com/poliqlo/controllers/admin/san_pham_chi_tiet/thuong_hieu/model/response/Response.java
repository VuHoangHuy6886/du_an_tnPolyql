package com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.poliqlo.models.ThuongHieu}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Response implements Serializable {
    @Positive
    private Integer id;
    @NotNull
    @Size(max = 255)
    @NotBlank
    private String ten;
    private String thumbnail;
    private Boolean isDeleted;
}