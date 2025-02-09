package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "san_pham_danh_muc")
public class SanPhamDanhMuc {
    @EmbeddedId
    private SanPhamDanhMucId id;

    @MapsId("idSanPham")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SAN_PHAM", nullable = false)
    private SanPham idSanPham;

    @MapsId("idDanhMuc")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_DANH_MUC", nullable = false)
    private DanhMuc idDanhMuc;

}