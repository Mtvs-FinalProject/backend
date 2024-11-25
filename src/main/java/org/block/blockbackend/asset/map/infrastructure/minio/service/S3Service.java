package org.block.blockbackend.asset.map.infrastructure.minio.service;

import lombok.extern.slf4j.Slf4j;
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
import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    private final String bucketName;

    private final String endpoint;



    public S3Service(@Value("${cloud.aws.region}") String region,
                     @Value("${cloud.aws.credentials.access-key}") String accessKey,
                     @Value("${cloud.aws.credentials.secret-key}") String secretKey,
                     @Value("${spring.profiles.active}") String profile,
                     @Value("${cloud.aws.s3.endpoint}") String endpoint,
                     @Value("${cloud.aws.s3.bucket}") String bucketName
                     ) {
        this.endpoint = endpoint;
        log.info("endpoint: {}", endpoint);
        this.bucketName = bucketName;
        if (!"prod".equals(profile)) {
            this.s3Client = S3Client.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)))
                    .region(Region.of(region))
                    .endpointOverride(URI.create(endpoint))
                    .forcePathStyle(true)
                    .build();
            try {
                // testConnection();
                // ensureBucketExists();
            } catch (Exception e) {
                log.error("Failed to initialize MinIO connection: {}", e.getMessage(), e);
                throw new RuntimeException("Storage initialization failed", e);
            }
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

    public void ensureBucketExistsWithPolicy() {
        try {
            // 버킷 존재 여부 확인
            try {
                s3Client.headBucket(HeadBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket '{}' already exists", bucketName);
            } catch (NoSuchBucketException e) {
                // 버킷이 없으면 생성
                s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Created new bucket: {}", bucketName);
            }

            // 버킷 정책 설정
            String policy = generateBucketPolicy(bucketName);
            setBucketPolicy(bucketName, policy);

        } catch (Exception e) {
            log.error("Error while checking/creating bucket: {}", e.getMessage());
            throw new RuntimeException("Bucket initialization failed", e);
        }
    }

    public void setBucketPolicy(String bucketName, String policyJson) {
        try {
            // 버킷 정책 설정 요청
            PutBucketPolicyRequest putBucketPolicyRequest = PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(policyJson) // JSON 형식의 정책 문자열
                    .build();

            s3Client.putBucketPolicy(putBucketPolicyRequest);
            log.info("Bucket policy applied successfully to bucket: {}", bucketName);
        } catch (Exception e) {
            log.error("Failed to apply bucket policy: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to apply bucket policy", e);
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

        ensureBucketExistsWithPolicy();

        // S3에 파일 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(image.getContentType())
                .build();


        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, image.getSize()));
        log.info("File uploaded successfully: {}", fileName);

        // S3 URL 추출
        result = endpoint + "/" + bucketName + "/" + fileName;

        return result;
    }

    // 확장자를 추출하는 메서드
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ""; // 확장자가 없으면 빈 문자열 반환
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1); // 마지막 "." 뒤의 문자열을 추출 (확장자)
    }

    public String generateBucketPolicy(String bucketName) {
        return  "{\n" +
                "   \"Version\":\"2012-10-17\",\n" +
                "   \"Statement\":[\n" +
                "      {\n" +
                "         \"Effect\":\"Allow\",\n" +
                "         \"Principal\":\"*\",\n" +
                "         \"Action\":[\"s3:GetObject\"],\n" +
                "         \"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                "      }\n" +
                "   ]\n" +
                "}";

    }

}
