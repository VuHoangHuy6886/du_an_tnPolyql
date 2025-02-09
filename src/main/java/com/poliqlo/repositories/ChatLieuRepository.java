package com.poliqlo.repositories;

import com.poliqlo.models.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {
    Collection<ChatLieu> findAllByIsDeletedIsFalseOrderByIdDesc();
}