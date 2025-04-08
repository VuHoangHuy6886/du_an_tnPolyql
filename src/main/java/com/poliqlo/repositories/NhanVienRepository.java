package com.poliqlo.repositories;

import com.poliqlo.models.NhanVien;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> , JpaSpecificationExecutor<NhanVien> {

    // Lọc những nhân viên chưa bị xóa
    Page<NhanVien> findAllByIsDeletedFalse(Pageable pageable);

    // Tìm nhân viên theo ID, chỉ lấy nhân viên chưa bị xóa
    Optional<NhanVien> findByIdAndIsDeletedFalse(Integer id);

    Page<NhanVien> findByIsDeletedTrue(Pageable pageable);

    default Page<NhanVien> findBySearch(String keyword, Pageable pageable) {
        return findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("taiKhoan").get("email")), searchPattern),
                        cb.like(cb.lower(root.get("taiKhoan").get("soDienThoai")), searchPattern),
                        cb.like(cb.lower(root.get("ten")), searchPattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
    // Thêm phương thức tìm kiếm nhân viên đã xóa
    default Page<NhanVien> findDeletedBySearch(String keyword, Pageable pageable) {
        return findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("isDeleted")));
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("taiKhoan").get("email")), searchPattern),
                        cb.like(cb.lower(root.get("taiKhoan").get("soDienThoai")), searchPattern),
                        cb.like(cb.lower(root.get("ten")), searchPattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }


}