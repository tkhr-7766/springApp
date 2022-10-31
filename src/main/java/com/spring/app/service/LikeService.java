package com.spring.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.entity.Like;
import com.spring.app.entity.Photo;
import com.spring.app.entity.User;
import com.spring.app.repository.LikeRepository;

@Service
public class LikeService {

    @Autowired
    LikeRepository likeRepository;

    public Like findByPhotoAndUser(Photo photo, User user) {

        try {
            return likeRepository.findByPhotoAndUser(photo, user);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void delete(Like like) {
        likeRepository.delete(like);
    }

    public Like save(Like like) {
        return likeRepository.save(like);
    }
}
