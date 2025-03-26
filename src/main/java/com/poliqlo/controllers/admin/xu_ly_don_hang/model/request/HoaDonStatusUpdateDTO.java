package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HoaDonStatusUpdateDTO {

    private String trangThai;

    private String ghiChu;
}
