package com.spring.app.controller.api;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.spring.app.entity.Like;
import com.spring.app.entity.Photo;
import com.spring.app.security.LoginUser;
import com.spring.app.service.LikeService;
import com.spring.app.service.PhotoService;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PhotoService photoService;

    @PutMapping("/photo/{id}/like")
    @ResponseStatus(value = HttpStatus.OK)
    public boolean like(@PathVariable("id") String id, @AuthenticationPrincipal LoginUser loginUser) {

        Photo photo = photoService.findById(id).get();
        Like like = likeService.findByPhotoAndUser(photo, loginUser.getUser());

        if (Objects.nonNull(like)) {
            likeService.delete(like);
        }

        Like newLike = new Like();
        newLike.setPhoto(photo);
        newLike.setUser(loginUser.getUser());
        likeService.save(newLike);

        return true;
    }

    @DeleteMapping("/photo/{id}/like")
    @ResponseStatus(value = HttpStatus.OK)
    public boolean unlike(@PathVariable("id") String id, @AuthenticationPrincipal LoginUser loginUser) {

        Photo photo = photoService.findById(id).get();
        Like like = likeService.findByPhotoAndUser(photo, loginUser.getUser());

        if (Objects.nonNull(like)) {
            likeService.delete(like);
        }

        return true;
    }
}
