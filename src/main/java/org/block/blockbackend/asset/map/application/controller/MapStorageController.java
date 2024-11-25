package org.block.blockbackend.asset.map.application.controller;

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

    public MapStorageController(MapApiService mapApiService) {
        this.mapApiService = mapApiService;
    }

   @Operation(summary = "맵 리스트 API", description = "등록된 맵리스트를 반환하는 API")
   @ApiResponse(responseCode = "200", description = "성공적으로 반환된 지도 목록",
           content = @Content(mediaType = "application/json",
                   examples = @ExampleObject(value = """
                            [
                              {
                                "mapName": "Seoul Map",
                                "price": 1000
                              },
                              {
                                "mapName": "Busan Map",
                                "price": 1500
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
    public ResponseEntity uploadMap(
            @UserIdFromToken UserId userId,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("data") UploadDTO uploadDTO
            ) {

        try {
            log.info("upload map...");
            mapApiService.uploadMapFile(userId.getUserId(), images, uploadDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

//     @Operation(summary="파일 다운로드 api", description="파일명으로 파일을 다운로드")
//     @GetMapping(value="/download")
//     public ResponseEntity downloadMap(@RequestParam Integer no) {
//
//         DownloadFileDTO response;
// //        InputStreamResource fileStream;
//         MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//
//         try {
//             log.info("download Map...");
//             response = mapApiService.purchaseMap(no);
// //            fileStream = new InputStreamResource(mapApiService.downloadMapFile(response.getFileName()));
//         } catch (Exception e) {
//             return ResponseEntity.badRequest().body(e.getMessage());
//         }
//
//         body.add("json", new HttpEntity<>(response, createJsonHeaders()));
// //        body.add("file", new HttpEntity<>(fileStream, createFileHeaders(response.getFileName())));
//
//         return ResponseEntity.ok()
// //                .contentType(MediaType.MULTIPART_MIXED)
//                 .body(body);
//     }

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
