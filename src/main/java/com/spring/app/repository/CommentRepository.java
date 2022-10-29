package com.spring.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}