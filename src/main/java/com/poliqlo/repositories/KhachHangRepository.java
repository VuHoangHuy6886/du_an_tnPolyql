package com.poliqlo.repositories;

import com.poliqlo.models.KhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>, JpaSpecificationExecutor<KhachHang> {
    Page<KhachHang> findAllByIsDeletedFalse(Pageable pageable);

    Optional<KhachHang> findByIdAndIsDeletedFalse(Integer id);

    Page<KhachHang> findByIsDeletedTrue(Pageable pageable);

    @Query("select kh from KhachHang kh where kh.taiKhoan.soDienThoai like :searchKey or kh.ten like :searchKey")
    Page<KhachHang> findKhachHangBySoDienThoai(String searchKey,Pageable pageable);
}