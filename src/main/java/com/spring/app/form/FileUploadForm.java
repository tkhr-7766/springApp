package com.spring.app.form;

import org.springframework.web.multipart.MultipartFile;

import com.spring.app.validate.FileRequired;
import com.spring.app.validate.MediaTypeImage;

import lombok.Data;

@Data
public class FileUploadForm {

    @MediaTypeImage
    @FileRequired
    private MultipartFile multipartFile;
}
