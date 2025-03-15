package com.poliqlo.repositories;

import com.poliqlo.models.KhachHang;
import com.poliqlo.models.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>, JpaSpecificationExecutor<KhachHang> {
    Page<KhachHang> findAllByIsDeletedFalse(Pageable pageable);

    Optional<KhachHang> findByIdAndIsDeletedFalse(Integer id);

    List<KhachHang> findByIsDeletedTrue();
    Page<KhachHang> findAll(Pageable pageable);

}