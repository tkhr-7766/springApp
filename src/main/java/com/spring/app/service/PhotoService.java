package com.spring.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.app.entity.Photo;
import com.spring.app.repository.PhotoRepository;

@Service
@Transactional
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public Page<Photo> findAll(Pageable pageable) {
        return photoRepository.findAll(pageable);
    }

    public Optional<Photo> findById(String id) {
        return photoRepository.findById(id);
    }
}
