package com.poliqlo.repositories;

import com.poliqlo.models.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KichThuocRepository extends JpaRepository<KichThuoc, Integer>, JpaSpecificationExecutor<KichThuoc> {
}