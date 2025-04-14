package com.poliqlo.repositories;

import com.poliqlo.models.ProductTemp;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductTempRepository extends JpaRepository<ProductTemp, Integer>, JpaSpecificationExecutor<ProductTemp> {
    @Query("delete from ProductTemp")
    @Modifying
    void clear();
    @Query("select coalesce(quantity,0) from ProductTemp where id = :id")
    @NotNull int getQuantity(Integer id);
}