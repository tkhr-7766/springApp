package com.spring.app.aws.s3;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

    private final S3Properties properties;

    private final AmazonS3 s3Client;

    public S3Service(S3Properties properties, AmazonS3 s3Client) {
        this.properties = properties;
        this.s3Client = s3Client;
    }

     /**
     * AWS S3 にファイルをアップロードします。
     * @param file
     * @param filename
     */
    public void uploadFile(MultipartFile file, String filename) {

        try (InputStream stream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            // リクエストヘッダにファイルサイズをセット
            metadata.setContentLength(file.getSize());

            // リクエスト用のオブジェクトを生成
            PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), filename, stream,
                    metadata);
            // ファイルを"公開状態"で保存するための設定
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

            // バケットが存在していなかったら新規作成
            if (!s3Client.doesBucketExistV2(properties.getBucketName())) {
                s3Client.createBucket(properties.getBucketName());
            }

            // アップロードの実行
            s3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new RuntimeException("S3へのファイル保存に失敗しました。");
        }
    }

    /**
     * S3パケットから指定ファイルを削除します。
     * @param filename
     */
    public void delete(String filename) {

        try {
            s3Client.deleteObject(properties.getBucketName(), filename);
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        }
    }

     /**
     * S3バケットに保存されているオブジェクトの公開URLを取得します。
     * @param filename
     * @return
     */
    public String getUrl(String filename) {
        return s3Client.getUrl(properties.getBucketName(), filename).toString();
    }

    /**
     * ファイル名からbyte形式でファイルを取得
     * @param filename
     * @return
     */
    public byte[] getFileByteArray(String filename) {
        try {
            String ext = FilenameUtils.getExtension(filename);
            URL url = new URL(getUrl(filename));
            BufferedImage readImage = ImageIO.read(url);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(readImage, ext, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

