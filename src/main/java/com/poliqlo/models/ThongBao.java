package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "thong_bao")
public class ThongBao {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TAI_KHOAN_ID", nullable = false)
    private TaiKhoan taiKhoan;

    @Size(max = 255)
    @Column(name = "URL")
    private String url;

    
    @Column(name = "NOI_DUNG",columnDefinition = "TEXT")
    private String noiDung;

    @NotNull
    @Column(name = "THOI_GIAN", nullable = false)
    private Instant thoiGian;

    @Size(max = 50)
    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}