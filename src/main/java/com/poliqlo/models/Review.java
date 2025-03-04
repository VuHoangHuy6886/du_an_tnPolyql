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
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SAN_PHAM_ID", nullable = false)
    private SanPham sanPham;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "KHACH_HANG_ID", nullable = false)
    private KhachHang khachHang;

    @NotNull
    @Column(name = "THOI_GIAN", nullable = false)
    private Instant thoiGian;

    
    @Column(name = "NOI_DUNG",columnDefinition = "TEXT")
    private String noiDung;

    @Size(max = 255)
    @Column(name = "ANH")
    private String anh;

    @Size(max = 50)
    @Column(name = "STATUS", length = 50)
    private String status;

    @ColumnDefault("0")
    @Column(name = "UP_VOTE")
    private Integer upVote;

    @ColumnDefault("0")
    @Column(name = "DOWN_VOTE")
    private Integer downVote;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}