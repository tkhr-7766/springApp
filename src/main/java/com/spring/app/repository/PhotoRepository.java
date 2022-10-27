package com.spring.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {
    public Page<Photo> findAll(Pageable pageable);
    public Optional<Photo> findById(String id);
}