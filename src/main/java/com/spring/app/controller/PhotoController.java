package com.spring.app.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.aws.s3.S3Service;
import com.spring.app.entity.Photo;
import com.spring.app.form.FileUploadForm;
import com.spring.app.security.LoginUser;
import com.spring.app.service.PhotoService;

@Controller
public class PhotoController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private PhotoService photoService;
    
    @GetMapping("/")
    public String index() {
        return "photo/index";
    }

    @GetMapping("/photo")
    public String upload(FileUploadForm fileUploadForm) {
        return "photo/upload";
    }

    @PostMapping("/photo")
    public String create(@Validated FileUploadForm fileUploadForm, BindingResult result, Model model,
            @AuthenticationPrincipal LoginUser loginUser) {

        if (result.hasErrors()) {
            return "photo/upload";
        }

        // フォームに送信されたファイルを取得
        MultipartFile file = fileUploadForm.getMultipartFile();
        // ファイルの情報から拡張子を取得
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());

        Photo photo = new Photo();

        // ID + 拡張子でファイル名を作成
        String filename = photo.getId() + "." + ext;

        photo.setFilename(filename);
        photo.setUser(loginUser.getUser());

        // ファイルアップロード
        s3Service.uploadFile(file, filename);

        try {
            // photosテーブルへファイル情報を保存
            photoService.save(photo);
        } catch (Exception e) {
            // DBへの登録に失敗したらS3からもファイルを削除
            s3Service.delete(filename);
            model.addAttribute("fileUploadError", "ファイルのアップロードに失敗しました");
            return "photo/upload";
        }

        return "redirect:/";
    }
}
