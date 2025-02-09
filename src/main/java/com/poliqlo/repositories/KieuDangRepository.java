package com.poliqlo.repositories;

import com.poliqlo.models.KieuDang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface KieuDangRepository extends JpaRepository<KieuDang, Integer> {
    Collection<KieuDang> findAllByIsDeletedIsFalseOrderByIdDesc();
}