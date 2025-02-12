package com.poliqlo.repositories;

import com.poliqlo.models.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MauSacRepository extends JpaRepository<MauSac, Integer>, JpaSpecificationExecutor<MauSac> {
}