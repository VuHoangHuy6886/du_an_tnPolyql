package com.poliqlo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "USER_NAME")
    private String userName;

    @Size(max = 255)
    @Column(name = "EMAIL")
    private String email;

    @Size(max = 20)
    @Column(name = "SO_DIEN_THOAI", length = 20)
    private String soDienThoai;

    @Size(max = 255)
    @Column(name = "ANH_URL")
    private String anhUrl;

    @Size(max = 50)
    @Column(name = "ROLE", length = 50)
    private String role;

    @Size(max = 255)
    @Column(name = "PASSWORD")
    private String password;

    @Size(max = 255)
    @Column(name = "GOOGLE_ID")
    private String googleId;

    @Size(max = 255)
    @Column(name = "FACEBOOK_ID")
    private String facebookId;

    @NotNull
    @ColumnDefault("b'1'")
    @Column(name = "IS_ENABLE", nullable = false)
    private Boolean isEnable = false;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getUsername() {
        return email;
    }
}