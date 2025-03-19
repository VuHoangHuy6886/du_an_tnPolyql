package com.poliqlo.repositories;

import com.poliqlo.models.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer>, JpaSpecificationExecutor<TaiKhoan> {

  Optional<TaiKhoan> findByEmail(String email);
  // Lọc những tài khoản chưa bị xóa
   // Tìm tài khoản theo ID, chỉ lấy tài khoản chưa bị xóa
   Optional<TaiKhoan> findByIdAndIsDeletedFalse(Integer id);

    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String soDienThoai);

    boolean existsByEmailAndIdNot(String email, Integer excludeId);
    boolean existsBySoDienThoaiAndIdNot(String soDienThoai, Integer excludeId);

}