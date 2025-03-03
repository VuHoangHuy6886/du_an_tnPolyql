package com.poliqlo.repositories;

import com.poliqlo.models.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    Collection<DanhMuc> findAllByIsDeletedIsFalseOrderByIdDesc();

}