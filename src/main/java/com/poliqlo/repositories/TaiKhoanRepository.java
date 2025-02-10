package com.poliqlo.repositories;

import com.poliqlo.models.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer>, JpaSpecificationExecutor<TaiKhoan> {
    Optional<TaiKhoan> findByEmail(String email);
}