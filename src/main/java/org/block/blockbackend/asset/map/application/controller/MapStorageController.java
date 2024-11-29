package org.block.blockbackend.asset.map.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.asset.map.application.dto.DetailDTO;
import org.block.blockbackend.asset.map.application.dto.DownloadFileDTO;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.application.service.MapApiService;
import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.block.blockbackend.core.config.UserIdFromToken;
import org.block.blockbackend.core.error.ApplicationException;
import org.block.blockbackend.user.dto.UserId;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "MAP API", description = "맵 데이터와 이미지등 정보를 업로드 & 다운로드")
@RestController
@RequestMapping("/api/v1/map")
public class MapStorageController {

    private final MapApiService mapApiService;
    private final ObjectMapper objectMapper;

    public MapStorageController(MapApiService mapApiService, ObjectMapper objectMapper) {
        this.mapApiService = mapApiService;
        this.objectMapper = objectMapper;
    }

   @Operation(summary = "맵 리스트 API", description = "등록된 맵리스트를 반환하는 API")
   @ApiResponse(responseCode = "200", description = "성공적으로 반환된 지도 목록",
           content = @Content(mediaType = "application/json",
                   examples = @ExampleObject(value = """
                            [
                              {
                                "no": 1,
                                "mapName": "Seoul Map",
                                "price": 1000,
                                "summary": "서울 지도"
                              },
                              {
                                "no": 2,
                                "mapName": "Busan Map",
                                "price": 1500,
                                "summary": "부산 지도"
                              }
                            ]
                            """)))
   @GetMapping("/maps")
   public ResponseEntity<?> maps() throws Exception {
         List<Map<String, Object>> result;

         result = mapApiService.getMapList();

         return ResponseEntity.ok().body(result);
   }


    @Operation(summary = "파일 업로드 api", description = "파일을 업로드 하는 api")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMap(
            @UserIdFromToken UserId userId,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("data") UploadDTO uploadDTO
            ) {

        try {
            log.info("data_table: {}", uploadDTO.getDataTable());
            String cleanedDataTable = uploadDTO.getDataTable().replaceAll("[\\r\\n\\t]", "");
            Map<String, Object> parsedDataTable = objectMapper.readValue(cleanedDataTable, Map.class);
            log.info("upload map...");
            return ResponseEntity.ok().body(mapApiService.uploadMapFile(userId.getUserId(), images, uploadDTO, parsedDataTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "맵 설명 API", description = "맵의 상세 정보를 조회하는 API")
    @GetMapping("/description")
    public ResponseEntity<?> description(@RequestParam Integer no) throws Exception {
        DetailDTO result;

        result = mapApiService.description(no);

        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "맵 데이터를 반환합니다", description = "맵 번호를 입력받아 데이터를 반환합니다")
    @GetMapping("/map-info")
    public ResponseEntity<?> mapInfo(@RequestParam Integer no) {
        return ResponseEntity.ok().body(mapApiService.findMapInfoByNo(no));
    }

    @Operation(summary = "맵 데이터를 반환합니다", description = "맵 번호를 입력받아 데이터를 반환합니다")
    @GetMapping("/map-info/name/{name}")
    public ResponseEntity<?> mapInfo(@PathVariable String name) {
        return ResponseEntity.ok().body(mapApiService.findMapInfoByMapName(name));
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
