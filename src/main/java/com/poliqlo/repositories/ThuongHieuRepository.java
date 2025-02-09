package com.poliqlo.repositories;

import com.poliqlo.models.ThuongHieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {
    Collection<ThuongHieu> findAllByIsDeletedIsFalseOrderByIdDesc();
}