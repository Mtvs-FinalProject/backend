package org.block.blockbackend.asset.map.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.block.blockbackend.asset.map.application.dto.DownloadFileDTO;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.application.service.MapApiService;
import org.block.blockbackend.core.error.ApplicationException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "MAP API", description = "맵 파일, 정보를 업로드 & 다운로드")
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
            @RequestPart("file") MultipartFile map,
            @RequestPart("data") UploadDTO uploadDTO
            ) {

        try {
            mapApiService.uploadMapFile(map, uploadDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary="파일 다운로드 api", description="파일명으로 파일을 다운로드")
    @GetMapping(value="/download")
    public ResponseEntity downloadMap(@RequestParam Integer no) {

        DownloadFileDTO json;
        InputStreamResource fileStream;
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        try {
            json = mapApiService.purchaseMap(no);
            fileStream = new InputStreamResource(mapApiService.downloadMapFile(json.getFileName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        body.add("json", new HttpEntity<>(json, createJsonHeaders()));
        body.add("file", new HttpEntity<>(fileStream, createFileHeaders(json.getFileName())));

        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_MIXED)
                .body(body);
    }

    // JSON 응답 헤더 생성
    private HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // 파일 응답 헤더 생성
    private HttpHeaders createFileHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        return headers;
    }
}
