package com.poliqlo.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link NhanVien}
 */
@Value
public class NhanVienDto implements Serializable {
    Integer id;
    @NotNull
    NhanVienDto.TaiKhoanDto taiKhoan;
    @NotNull
    @Size(max = 255)
    String ten;
    @NotNull
    Boolean isDeleted;

    /**
     * DTO for {@link TaiKhoan}
     */
    @Value
    public static class TaiKhoanDto implements Serializable {
        Integer id;
        @Size(max = 255)
        String userName;
        @Size(max = 255)
        String email;
        @Size(max = 20)
        String soDienThoai;
        @Size(max = 255)
        String anhUrl;
        @Size(max = 50)
        String role;
        @Size(max = 255)
        String password;
        @Size(max = 255)
        String googleId;
        @Size(max = 255)
        String facebookId;
        @NotNull
        Boolean isEnable;
        @NotNull
        Boolean isDeleted;
    }
}