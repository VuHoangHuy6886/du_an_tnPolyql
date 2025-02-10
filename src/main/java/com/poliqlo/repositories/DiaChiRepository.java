package com.poliqlo.repositories;

import com.poliqlo.models.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiaChiRepository extends JpaRepository<DiaChi, Integer>, JpaSpecificationExecutor<DiaChi> {
}