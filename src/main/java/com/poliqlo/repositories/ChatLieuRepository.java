package com.poliqlo.repositories;

import com.poliqlo.models.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> , JpaSpecificationExecutor<ChatLieu> {
  }