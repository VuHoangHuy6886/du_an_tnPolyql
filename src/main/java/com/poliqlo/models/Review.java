package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SAN_PHAM", nullable = false)
    private SanPham idSanPham;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_KHACH_HANG", nullable = false)
    private KhachHang idKhachHang;

    @Column(name = "THOI_GIAN", nullable = false)
    private Instant thoiGian;

    @Lob
    @Column(name = "NOI_DUNG",length = 500)
    private String noiDung;

    @Column(name = "ANH")
    private String anh;

    @Column(name = "STATUS", length = 50)
    private String status;

    @ColumnDefault("0")
    @Column(name = "UP_VOTE")
    private Integer upVote;

    @ColumnDefault("0")
    @Column(name = "DOWN_VOTE")
    private Integer downVote;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}