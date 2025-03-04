package com.poliqlo.controllers.admin.san_pham.model.reponse;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class ResponseDataList {
    List<String> code;
    List<DataList> sanPham;
    List<DataList> chatLieu;
    List<DataList> danhMuc;
    List<DataList> kichThuoc;
    List<DataList> kieuDang;
    List<DataList> mauSac;
    List<DataList> thuongHieu;
}

