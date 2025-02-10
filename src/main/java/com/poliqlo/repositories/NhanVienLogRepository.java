package com.poliqlo.repositories;

import com.poliqlo.models.NhanVienLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NhanVienLogRepository extends JpaRepository<NhanVienLog, Integer>, JpaSpecificationExecutor<NhanVienLog> {
}