package com.poliqlo.repositories;

import com.poliqlo.models.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    Collection<MauSac> findAllByIsDeletedIsFalseOrderByIdDesc();
}