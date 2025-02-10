package com.poliqlo.repositories;

import com.poliqlo.models.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BannerRepository extends JpaRepository<Banner, Integer>, JpaSpecificationExecutor<Banner> {
}