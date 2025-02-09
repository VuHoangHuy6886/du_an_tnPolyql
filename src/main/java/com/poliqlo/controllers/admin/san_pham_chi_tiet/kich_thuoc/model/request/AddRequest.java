package com.poliqlo.controllers.admin.san_pham_chi_tiet.kich_thuoc.model.request;

import com.poliqlo.models.KichThuoc;
import com.poliqlo.validation.Unique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link KichThuoc}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddRequest implements Serializable {
    @NotNull(message = "Tên không được trống")
    @Size(max = 255, message = "Độ dài vượt quá 255 ký tự")
    @NotBlank(message = "Tên không được để trống")
    @Unique(message = "Tên đã tồn tại",fieldName = "ten",entityClass = KichThuoc.class)
    private String ten;
    final Boolean isDeleted=false;
}