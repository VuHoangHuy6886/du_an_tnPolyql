package com.poliqlo.repositories;

import com.poliqlo.models.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> , JpaSpecificationExecutor<NhanVien> {

    // Lọc những nhân viên chưa bị xóa
    Page<NhanVien> findAllByIsDeletedFalse(Pageable pageable);

    // Tìm nhân viên theo ID, chỉ lấy nhân viên chưa bị xóa
    Optional<NhanVien> findByIdAndIsDeletedFalse(Integer id);

    List<NhanVien> findByIsDeletedTrue();



}