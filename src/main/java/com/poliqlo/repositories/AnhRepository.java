package com.poliqlo.repositories;

import com.poliqlo.models.Anh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnhRepository extends JpaRepository<Anh, Integer>, JpaSpecificationExecutor<Anh> {
}