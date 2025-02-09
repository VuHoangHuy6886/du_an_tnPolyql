package com.poliqlo.controllers.admin.san_pham_chi_tiet.thuong_hieu.model.request;

import com.poliqlo.models.ThuongHieu;
import com.poliqlo.validation.ExistsId;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.poliqlo.models.ThuongHieu}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EditReq implements Serializable {
    @NotNull
    @Positive
    @ExistsId(entityClass = ThuongHieu.class)
    Integer id;
    @NotNull(message = "Tên không được trống")
    @Size(max = 255)
    @NotBlank(message = "Tên không được trống")
    private String ten;
    private MultipartFile thumbnail;
    @NotNull(message = "Trạng thái không được để trống")
    private Boolean isDeleted;
}