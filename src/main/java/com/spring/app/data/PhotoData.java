package com.spring.app.data;

import com.spring.app.entity.Photo;
import com.spring.app.entity.User;

import lombok.Data;

@Data
public class PhotoData {

    private String url;

    private Photo photo;

    private int likesCount;

    private boolean likedByUser;

    public PhotoData(Photo photo, String url, User user) {
        setPhoto(photo);
        setUrl(url);
        setLikesCount(photo.getLikes().size());
        setLikedByUser(photo.getLikes().stream().anyMatch(like -> like.getUser().getId().equals(user.getId())));
    }
}