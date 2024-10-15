package org.block.blockbackend2.config;

import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String defaultBucket;

    public MinioService(@Value("${spring.minio.endpoint}") String endpoint,
                        @Value("${spring.minio.access-key}") String accessKey,
                        @Value("${spring.minio.secret-key}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public void uploadFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        // 버킷이 존재하는지 확인
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(defaultBucket).build());
        if (!isExist) {
            // 버킷이 존재하지 않으면 생성
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(defaultBucket).build());
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(defaultBucket)
                        .object(fileName)
                        .stream(inputStream, file.getSize(), -1)
                        // -1: stream 이 끝날때까지 업로드
                        .contentType(file.getContentType())
                        .build()
        );
        
        log.info("File uploaded successfully: {}", fileName);
    }

    public InputStream downloadFile(String fileName) throws Exception {
        // MinIO에서 파일 가져오기
        log.info("File downloaded: {}", fileName);
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(defaultBucket)
                        .object(fileName)
                        .build()
        );
    }
}
