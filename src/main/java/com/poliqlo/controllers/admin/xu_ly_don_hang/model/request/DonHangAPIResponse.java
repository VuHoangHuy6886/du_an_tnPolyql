package com.poliqlo.controllers.admin.xu_ly_don_hang.model.request;


import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonHangAPIResponse implements Serializable {
    private String diaChi;
    private String soDienThoai;
    private BigDecimal phiVanChuyen;
    private String ghiChu;
}
