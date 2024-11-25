// package org.block.blockbackend.asset.map.infrastructure.minio.service;
//
// import io.minio.*;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
//
// import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.UUID;
//
// @Service
// @Slf4j
// public class MinioService {
//
//     private final MinioClient minioClient;
//
//     @Value("${spring.minio.bucket.bloc}")
//     private String blocBucket;
//
//     @Value("${spring.minio.bucket.image}")
//     private String imageBucket;
//
//     public MinioService(@Value("${spring.minio.endpoint}") String endpoint,
//                         @Value("${spring.minio.access-key}") String accessKey,
//                         @Value("${spring.minio.secret-key}") String secretKey) {
//         this.minioClient = MinioClient.builder()
//                 .endpoint(endpoint)
//                 .credentials(accessKey, secretKey)
//                 .build();
//     }
//
//     public String uploadFile(MultipartFile file) throws Exception {
//         String originalFilename = file.getOriginalFilename();
//         InputStream inputStream = file.getInputStream();
//
//         System.out.println("Original Filename: " + originalFilename);
//
//         // 파일명 변경: UUID를 사용해 고유한 이름 생성 (원본 파일 확장자 유지)
//         String fileExtension = getFileExtension(originalFilename); // 확장자 추출
//         String fileName = UUID.randomUUID().toString() + "." + fileExtension; // 새 파일명 생성
//         System.out.println("New Filename: " + fileName);
//
//         /* 파일을 저장하는게 아니라 파일명만 수정해야함 */
//         // 변경된 파일명으로 파일 저장
// //        saveFile(fileName, file); // 파일 저장하는 로직
//
//         // 버킷이 존재하는지 확인
//         boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(blocBucket).build());
//         if (!isExist) {
//             // 버킷이 존재하지 않으면 생성
//             minioClient.makeBucket(MakeBucketArgs.builder().bucket(blocBucket).build());
//         }
//
//         minioClient.putObject(
//                 PutObjectArgs.builder()
//                         .bucket(blocBucket)
//                         .object(fileName)
//                         .stream(inputStream, file.getSize(), -1)
//                         // -1: stream 이 끝날때까지 업로드
//                         .contentType(file.getContentType())
//                         .build()
//         );
//
//         log.info("File uploaded successfully: {}", fileName);
//
//         return fileName;
//     }
//
//     public InputStream downloadFile(String fileName) throws Exception {
//         // MinIO에서 파일 가져오기
//         log.info("File downloaded: {}", fileName);
//         return minioClient.getObject(
//                 GetObjectArgs.builder()
//                         .bucket(blocBucket)
//                         .object(fileName)
//                         .build()
//         );
//     }
//
//     // 확장자를 추출하는 메서드
//     private String getFileExtension(String fileName) {
//         if (fileName == null || !fileName.contains(".")) {
//             return ""; // 확장자가 없으면 빈 문자열 반환
//         }
//         return fileName.substring(fileName.lastIndexOf(".") + 1); // 마지막 "." 뒤의 문자열을 추출 (확장자)
//     }
//
//     /* 수정 필요 */
//     // 파일을 저장하는 메서드
// //    private void saveFile(String newFileName, MultipartFile file) throws IOException {
// //        String uploadDir = "/path/to/upload/directory"; // 실제 저장할 경로 설정
// //        File destinationFile = new File(uploadDir, newFileName); // 새로운 파일명으로 파일 객체 생성
// //        file.transferTo(destinationFile); // 파일을 저장
// //    }
// }
