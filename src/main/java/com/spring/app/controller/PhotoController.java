package com.spring.app.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.app.aws.s3.S3Service;
import com.spring.app.data.PhotoData;
import com.spring.app.entity.Comment;
import com.spring.app.entity.Photo;
import com.spring.app.exception.NotFoundException;
import com.spring.app.form.AddCommentForm;
import com.spring.app.form.FileUploadForm;
import com.spring.app.security.LoginUser;
import com.spring.app.service.CommentService;
import com.spring.app.service.PhotoService;

@Controller
public class PhotoController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private CommentService commentService;
    
    @GetMapping("/")
    public String index(
            @PageableDefault(page = 0, size = 12, direction = Direction.DESC, sort = { "created" }) Pageable pageable,
            Model model, @AuthenticationPrincipal LoginUser loginUser) {

        // photosテーブルから一覧を取得
        // 引数として受け取っているPageableクラスを渡すことでページネート機能を実現している
        Page<Photo> photos = photoService.findAll(pageable);

        // PhotoDataを中にもつListクラスのインスタンスを用意
        List<PhotoData> photoList = new ArrayList<>();

        // photosテーブルのfilenameから公開URLを取得し、PhotoDataのインスタンスに格納
        for (Photo photo : photos) {
            PhotoData photoData = new PhotoData(photo, s3Service.getUrl(photo.getFilename()), loginUser.getUser());
            photoList.add(photoData);
        }

        model.addAttribute("page", photos);
        model.addAttribute("photos", photoList);

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

    @GetMapping("/photo/{id}/download")
    public void download(@PathVariable("id") String id, HttpServletResponse response) {

        // IDからファイル名を取得
        Photo photo = photoService.findById(id).get();
        String filename = photo.getFilename();

        // ファイル名からダウンロード対象のバイナリデータを取得
        byte[] binary = s3Service.getFileByteArray(filename);

        /**
         * レスポンスの内容を Web ページとして表示するのではなく
         * ダウンロードさせるための保存ダイアログを開くようにブラウザに指示
         */
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentLength(binary.length);

        OutputStream os = null;

        // レスポンスにバイナリデータを書き込む
        try {
            os = response.getOutputStream();
            os.write(binary);
            os.flush();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    @GetMapping("/photo/{id}")
    public String show(@PathVariable("id") String id, Model model, @AuthenticationPrincipal LoginUser loginUser)
            throws NotFoundException {
        try {
            if (!model.containsAttribute("item")) {
                PhotoData photoData = this.getPhotoData(id, loginUser);
                model.addAttribute("item", photoData);
                model.addAttribute("addCommentForm", new AddCommentForm());
            }

            return "photo/show";
        } catch (NoSuchElementException e) {
            throw new NotFoundException("データが存在しません。");
        }
    }

    @PostMapping("/photo/{id}/comment")
    public String addComment(@Validated AddCommentForm addCommentForm, BindingResult result,
            @PathVariable("id") String id, @AuthenticationPrincipal LoginUser loginUser,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addCommentForm", result);
            PhotoData photoData = this.getPhotoData(id, loginUser);
            redirectAttributes.addFlashAttribute("item", photoData);
            return "redirect:/photo/" + id;
        }

        try {
            Photo photo = photoService.findById(id).get();
            Comment comment = new Comment();
            comment.setContent(addCommentForm.getContent());
            comment.setUser(loginUser.getUser());
            comment.setPhoto(photo);

            commentService.save(comment);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("データが存在しません。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("addCommentError", "コメント保存に失敗しました。");
            PhotoData photoData = this.getPhotoData(id, loginUser);
            redirectAttributes.addFlashAttribute("item", photoData);
        }

        return "redirect:/photo/" + id;
    }

    private PhotoData getPhotoData(String id, LoginUser loginUser) {
        Photo photo = photoService.findById(id).get();

        Collections.sort(photo.getComments(), new Comparator<Comment>() {
            @Override
            public int compare(Comment obj1, Comment obj2) {
                return obj2.getId().compareTo(obj1.getId());
            }
        });

        String url = s3Service.getUrl(photo.getFilename());

        PhotoData photoData = new PhotoData(photo, url, loginUser.getUser());

        return photoData;
    }
}
