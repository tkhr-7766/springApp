package com.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.entity.Like;
import com.spring.app.entity.Photo;
import com.spring.app.entity.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findByPhotoAndUser(Photo photo, User user);
}
