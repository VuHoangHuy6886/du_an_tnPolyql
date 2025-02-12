package com.poliqlo.repositories;

import com.poliqlo.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReviewRepository extends JpaRepository<Review, Integer> , JpaSpecificationExecutor<Review> {
  }