package com.poliqlo.controllers.admin.nhan_vien.model.request;

import com.poliqlo.models.NhanVien;
import com.poliqlo.validation.Unique;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link com.poliqlo.models.TaiKhoan}
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AddRequest implements Serializable {
    @NotNull(message = "Tên không được để trống")
    @Size(min = 1, max = 100, message = "Tên phải có độ dài từ 1 đến 100 ký tự")
    private String ten;

//    // Kiểm tra username chỉ chứa các ký tự hợp lệ (chữ cái, chữ số, dấu gạch dưới, dấu chấm, dấu gạch nối)
//    @NotNull(message = "Username không được để trống")
//    @Pattern(regexp = "^[a-zA-Z0-9_.-]{5,50}$", message = "Username phải chứa từ 5 đến 50 ký tự và chỉ được phép bao gồm chữ cái, chữ số, dấu chấm, gạch dưới hoặc dấu gạch nối")
//    private String username;

    @NotNull(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84|\\(\\+84\\))\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    private String soDienThoai;

    @NotNull(message = "Role không được để trống")
    @Pattern(regexp = "^ROLE_.{1,10}$", message = "Role phải bắt đầu với ROLE_ và có tối đa 10 ký tự sau ROLE_")
    private String role;

    private MultipartFile anhUrl;

    @NotNull(message = "Password không được để trống")
    @Size(min = 8, max = 20, message = "Password phải có độ dài từ 8 đến 20 ký tự")
    private String password;
}