package com.poliqlo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class SanPhamDanhMucId implements Serializable {
    private static final long serialVersionUID = -1769916383069947599L;
    @Column(name = "ID_SAN_PHAM", nullable = false)
    private Integer idSanPham;

    @Column(name = "ID_DANH_MUC", nullable = false)
    private Integer idDanhMuc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SanPhamDanhMucId entity = (SanPhamDanhMucId) o;
        return Objects.equals(this.idDanhMuc, entity.idDanhMuc) &&
                Objects.equals(this.idSanPham, entity.idSanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDanhMuc, idSanPham);
    }

}