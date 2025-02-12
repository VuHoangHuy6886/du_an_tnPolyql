package com.poliqlo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class SanPhamDanhMucId implements Serializable {
    private static final long serialVersionUID = 592997867485441534L;
    @NotNull
    @Column(name = "SAN_PHAM_ID", nullable = false)
    private Integer sanPhamId;

    @NotNull
    @Column(name = "DANH_MUC_ID", nullable = false)
    private Integer danhMucId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SanPhamDanhMucId entity = (SanPhamDanhMucId) o;
        return Objects.equals(this.sanPhamId, entity.sanPhamId) &&
                Objects.equals(this.danhMucId, entity.danhMucId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sanPhamId, danhMucId);
    }

}