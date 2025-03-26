package com.poliqlo.controllers.admin.ban_hang.model.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.poliqlo.models.KhachHang}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangDto implements Serializable,Select2Response {
    Integer id;
    String taiKhoanEmail;
    String taiKhoanSoDienThoai;
    Boolean taiKhoanIsEnable;
    @NotNull
    @Size(max = 255)
    String ten;

    @Override
    public String getText() {
        return ten;
    }
    @Override
    public String getId() {
        return id+"";
    }
}