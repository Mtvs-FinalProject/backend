package org.block.blockbackend.asset.map.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.application.service.MapApiService;
import org.block.blockbackend.core.error.ApplicationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "MAP API", description = "맵 파일, 정보를 업로드 & 다운로드")
@RestController
@RequestMapping("/api/vi/map")
public class MapStorageController {

    MapApiService mapApiService;

    public MapStorageController(MapApiService mapApiService) {
        this.mapApiService = mapApiService;
    }

    @Operation(summary = "파일 업로드 api", description = "파일을 업로드 하는 api")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(
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
}
