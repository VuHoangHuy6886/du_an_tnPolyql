package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "thong_bao")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_TAI_KHOAN", nullable = false)
    private TaiKhoan idTaiKhoan;

    @Column(name = "URL")
    private String url;

    @Lob
    @Column(name = "NOI_DUNG",length = 500)
    private String noiDung;

    @Column(name = "THOI_GIAN", nullable = false)
    private Instant thoiGian;

    @Column(name = "TRANG_THAI", length = 50)
    private String trangThai;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}