package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false , cascade = CascadeType.ALL)
    @JoinColumn(name = "TAI_KHOAN_ID", nullable = false)
    private TaiKhoan taiKhoan;

    @Size(max = 255)
    @NotNull
    @Column(name = "TEN", nullable = false)
    private String ten;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;
    public String getMaNhanVien() {
        return "NV" + (id == null ? "" : id.toString());
    }

}