    package com.poliqlo.models;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import jakarta.validation.constraints.Size;
    import lombok.*;
    import org.hibernate.annotations.ColumnDefault;

    import java.time.LocalDate;
    import java.util.ArrayList;
    import java.util.List;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name = "khach_hang")
    public class KhachHang {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID", nullable = false)
        private Integer id;

        @NotNull
        @OneToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "TAI_KHOAN_ID", nullable = false)
        @JsonIgnore
        private TaiKhoan taiKhoan;

        @Size(max = 255)
        @NotNull
        @Column(name = "TEN", nullable = false)
        private String ten;

        @Column(name = "NGAY_SINH")
        private LocalDate ngaySinh;

        @Size(max = 10)
        @Column(name = "GIOI_TINH", length = 10)
        private String gioiTinh;

        @ColumnDefault("0")
        @Column(name = "SO_LAN_TU_CHOI_NHAN_HANG")
        private Integer soLanTuChoiNhanHang;

        @NotNull
        @ColumnDefault("b'0'")
        @Column(name = "IS_DELETED", nullable = false)
        private Boolean isDeleted = false;
        @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<DiaChi> danhSachDiaChi = new ArrayList<>();


    }