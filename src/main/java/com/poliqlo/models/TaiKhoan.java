package com.poliqlo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SO_DIEN_THOAI", length = 20)
    private String soDienThoai;

    @Column(name = "ANH_URL")
    private String anhUrl;

    @Column(name = "ROLE", length = 50)
    private String role;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "GOOGLE_ID")
    private String googleId;

    @Column(name = "FACEBOOK_ID")
    private String facebookId;

    @ColumnDefault("b'1'")
    @Column(name = "IS_ENABLE", nullable = false)
    private Boolean isEnable = false;

    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

}