package com.poliqlo.controllers.admin.ban_hang.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.poly.polystore.entity.KhachHang}
 */
@Data
public class CustomerDto implements Serializable {
    Integer id;
    String email;
   String ten;
    String soDienThoai;
    Integer deleted;
}