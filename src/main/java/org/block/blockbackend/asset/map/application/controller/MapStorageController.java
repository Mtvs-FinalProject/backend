package org.block.blockbackend.asset.map.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.asset.map.application.dto.DetailDTO;
import org.block.blockbackend.asset.map.application.dto.DownloadFileDTO;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.application.service.MapApiService;
import org.block.blockbackend.core.error.ApplicationException;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "MAP API", description = "맵 데이터와 이미지등 정보를 업로드 & 다운로드")
@RestController
@RequestMapping("/api/v1/map")
public class MapStorageController {

    MapApiService mapApiService;

    public MapStorageController(MapApiService mapApiService) {
        this.mapApiService = mapApiService;
    }

    @Operation(summary = "파일 업로드 api", description = "파일을 업로드 하는 api")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadMap(
            @RequestPart("file") List<MultipartFile> images,
            @RequestPart("data") UploadDTO uploadDTO
            ) {

        try {
            log.info("upload map...");
            mapApiService.uploadMapFile(images, uploadDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary="파일 다운로드 api", description="파일명으로 파일을 다운로드")
    @GetMapping(value="/download")
    public ResponseEntity downloadMap(@RequestParam Integer no) {

        DownloadFileDTO response;
//        InputStreamResource fileStream;
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            log.info("download Map...");
            response = mapApiService.purchaseMap(no);
//            fileStream = new InputStreamResource(mapApiService.downloadMapFile(response.getFileName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        body.add("json", new HttpEntity<>(response, createJsonHeaders()));
//        body.add("file", new HttpEntity<>(fileStream, createFileHeaders(response.getFileName())));

        return ResponseEntity.ok()
//                .contentType(MediaType.MULTIPART_MIXED)
                .body(body);
    }

    @Operation(summary = "맵 설명 API", description = "맵의 상세 정보를 조회하는 API")
    @GetMapping("/description")
    public ResponseEntity<?> description(@RequestParam Integer no) throws Exception {
        DetailDTO result;

        result = mapApiService.description(no);

        return ResponseEntity.ok().body(result);
    }

    /* 여기 부터는 헤더 설정 */

    // JSON 응답 헤더 생성
    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 파일을 응답하는 응답 헤더 생성
    private HttpHeaders createFileHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        return headers;
    }
}
