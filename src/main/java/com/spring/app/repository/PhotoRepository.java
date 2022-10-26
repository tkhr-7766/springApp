package com.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.entity.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

}