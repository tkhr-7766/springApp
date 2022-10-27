package com.spring.app.data;

import com.spring.app.entity.Photo;

import lombok.Data;

@Data
public class PhotoData {

    private String url;

    private Photo photo;

    public PhotoData(Photo photo, String url) {
        setPhoto(photo);
        setUrl(url);
    }
}