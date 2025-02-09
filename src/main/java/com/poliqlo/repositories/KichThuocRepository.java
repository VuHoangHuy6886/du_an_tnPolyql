package com.poliqlo.repositories;

import com.poliqlo.models.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface KichThuocRepository extends JpaRepository<KichThuoc, Integer> {
    Collection<KichThuoc> findAllByIsDeletedIsFalseOrderByIdDesc();
}