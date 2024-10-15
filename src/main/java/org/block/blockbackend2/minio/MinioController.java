package org.block.blockbackend2.minio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.block.blockbackend2.config.MinioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File API", description = "파일 업로드 및 다운로드를 위한 API") // Swagger 명세용 태그 추가
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 업로드", description = "원하는 파일을 서버로 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 업로드 성공",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "success"))),
            @ApiResponse(responseCode = "500", description = "파일 업로드 실패",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(value = "File upload failed")))
    })
    public ResponseEntity<String> upload(
            @Parameter(description = "업로드할 파일", required = true)
            @RequestParam MultipartFile file) {
        try {
            minioService.uploadFile(file);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    @GetMapping("/{fileName}")
    @Operation(summary = "파일 다운로드", description = "서버로부터 파일을 다운로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공",
                    content = @Content(mediaType = "application/octet-stream", examples = @ExampleObject(value = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA=="))),
            @ApiResponse(responseCode = "500", description = "파일 다운로드 실패",
                    content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "File download failed")))
    })    public ResponseEntity<?> downloadFile(
            @Parameter(description = "다운로드할 파일 이름", required = true)
            @PathVariable String fileName) throws Exception {
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(minioService.downloadFile(fileName)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File download failed");
        }
    }
}