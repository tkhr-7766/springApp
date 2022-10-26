package com.spring.app.validate;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MediaTypeImageValidator implements ConstraintValidator<MediaTypeImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {


        if (file == null || file.getOriginalFilename().isEmpty()) {
            return true;
        }

        MediaType mediaType = MediaType.parseMediaType(file.getContentType());

        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        List<MediaType> mediaTypeList = Arrays.asList(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG, MediaType.IMAGE_GIF);
        List<String> extList = Arrays.asList("jpg", "jpeg", "png", "gif");

        return mediaTypeList.stream().anyMatch((mType) -> mediaType.includes(mType))
                && extList.stream().anyMatch((v) -> ext.toLowerCase().equals(v));
    }
}
