package com.poliqlo.controllers.admin.nhan_vien.model.response;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.poliqlo.models.NhanVien}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response implements Serializable {
    Integer id;
    TaiKhoanDto taiKhoan;
    String ten;

    /**
     * DTO for {@link com.poliqlo.models.TaiKhoan}
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class TaiKhoanDto implements Serializable {
        Integer id;
        String email;
        String soDienThoai;
        String anhUrl;
        String role;
        String googleId;
        String facebookId;
        Boolean isEnable;
    }
}