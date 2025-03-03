package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "san_pham_danh_muc")
public class SanPhamDanhMuc {
    @EmbeddedId
    private SanPhamDanhMucId id;

    @MapsId("sanPhamId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SAN_PHAM_ID", nullable = false)
    private SanPham sanPham;

    @MapsId("danhMucId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DANH_MUC_ID", nullable = false)
    private DanhMuc danhMuc;

}