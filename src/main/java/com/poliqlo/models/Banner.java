package com.poliqlo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ANH_URL", nullable = false)
    private String anhUrl;

    @Column(name = "LINK_URL", nullable = false)
    private String linkUrl;

    @Column(name = "THOI_GIAN_BAT_DAU", nullable = false)
    private Instant thoiGianBatDau;

    @Column(name = "THOI_GIAN_KET_THUC", nullable = false)
    private Instant thoiGianKetThuc;

    @ColumnDefault("0")
    @Column(name = "PRIORITY")
    private Integer priority;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}