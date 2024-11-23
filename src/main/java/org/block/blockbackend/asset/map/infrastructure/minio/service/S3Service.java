package org.block.blockbackend.asset.map.infrastructure.minio.service;

import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.core.utils.ApplicationProfileCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;


    public S3Service(@Value("${cloud.aws.region}") String region,
                     @Value("${cloud.aws.credentials.access-key}") String accessKey,
                     @Value("${cloud.aws.credentials.secret-key}") String secretKey) {
        if (ApplicationProfileCheck.isProd()) {
            // MinIO 클라이언트 생성
            this.s3Client =  S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .region(Region.of(region)) // MinIO는 임의의 리전
                    .endpointOverride(URI.create(endpoint)) // MinIO 엔드포인트
                    .build();
        } else {
            // AWS S3 클라이언트 생성
            this.s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                    .build();
        }
    }

    public String uploadFile(MultipartFile image) throws Exception {

        String result;

        String originalFilename = image.getOriginalFilename();
        InputStream inputStream = image.getInputStream();
        System.out.println("Original Filename: " + originalFilename);

        // 파일명 변경: UUID를 사용해 고유한 이름 생성 (원본 파일 확장자 유지)
        String fileExtension = getFileExtension(originalFilename); // 확장자 추출
        String fileName = UUID.randomUUID().toString() + "." + fileExtension; // 새 파일명 생성
        System.out.println("New Filename: " + fileName);

        // S3에 파일 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(image.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, image.getSize()));
        log.info("File uploaded successfully: {}", fileName);

        // S3 URL 추출
        result = endpoint + "/" + fileName;

        return result;
    }

//    public InputStream downloadFile(String fileName) throws Exception {
//        // S3에서 파일 가져오기
//        log.info("Downloading file from S3: {}", fileName);
//
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileName)
//                .build();
//
//        return s3Client.getObject(getObjectRequest);
//    }

    // 확장자를 추출하는 메서드
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ""; // 확장자가 없으면 빈 문자열 반환
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1); // 마지막 "." 뒤의 문자열을 추출 (확장자)
    }
}
