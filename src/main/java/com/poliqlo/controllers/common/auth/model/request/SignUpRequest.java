package com.poliqlo.controllers.common.auth.model.request;

import com.poliqlo.models.TaiKhoan;
import com.poliqlo.validation.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "Vui lòng không để trống trường này")
    String ten;
    @Unique(message = "Số điện thoại đã tồn tại",fieldName = "soDienThoai",entityClass = TaiKhoan.class)
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})",message = "Số điện thoại không đúng định dạng")
    String soDienThoai;
    @Unique(message = "Email đã tồn tại",fieldName = "email",entityClass = TaiKhoan.class)
    @Email(message = "Email không đúng định dạng")
    String email;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Length(min = 6, max = 16,message = "Mật khẩu từ 6-12 ký tự")
    String matKhau;
}

