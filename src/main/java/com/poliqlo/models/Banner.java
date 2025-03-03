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
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "ANH_URL", nullable = false)
    private String anhUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "LINK_URL", nullable = false)
    private String linkUrl;

    @NotNull
    @Column(name = "THOI_GIAN_BAT_DAU", nullable = false)
    private Instant thoiGianBatDau;

    @NotNull
    @Column(name = "THOI_GIAN_KET_THUC", nullable = false)
    private Instant thoiGianKetThuc;

    @ColumnDefault("0")
    @Column(name = "PRIORITY")
    private Integer priority;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}