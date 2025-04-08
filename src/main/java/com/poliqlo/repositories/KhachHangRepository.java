package com.poliqlo.repositories;

import com.poliqlo.models.KhachHang;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>, JpaSpecificationExecutor<KhachHang> {
    Page<KhachHang> findAllByIsDeletedFalse(Pageable pageable);

    Optional<KhachHang> findByIdAndIsDeletedFalse(Integer id);

    Page<KhachHang> findByIsDeletedTrue(Pageable pageable);

    @Query("select kh from KhachHang kh where kh.taiKhoan.soDienThoai like :searchKey or kh.ten like :searchKey")
    Page<KhachHang> findKhachHangBySoDienThoai(String searchKey,Pageable pageable);

    // Phương thức mặc định sử dụng Specification
    default Page<KhachHang> findBySearchAndFilter(String keyword, String gioiTinh, Pageable pageable) {
        return findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Điều kiện isDeleted = false
            predicates.add(cb.isFalse(root.get("isDeleted")));

            // Tìm kiếm theo email, số điện thoại hoặc tên
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("taiKhoan").get("email")), searchPattern),
                        cb.like(cb.lower(root.get("taiKhoan").get("soDienThoai")), searchPattern),
                        cb.like(cb.lower(root.get("ten")), searchPattern)
                ));
            }

            // Lọc theo giới tính
            if (gioiTinh != null && !gioiTinh.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("gioiTinh"), gioiTinh));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
    default Page<KhachHang> findDeletedBySearchAndFilter(String keyword, String gioiTinh, Pageable pageable) {
        return findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("isDeleted"))); // Chỉ lấy khách hàng đã xóa
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("taiKhoan").get("email")), searchPattern),
                        cb.like(cb.lower(root.get("taiKhoan").get("soDienThoai")), searchPattern),
                        cb.like(cb.lower(root.get("ten")), searchPattern)
                ));
            }
            if (gioiTinh != null && !gioiTinh.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("gioiTinh"), gioiTinh));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

}