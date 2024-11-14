package org.block.blockbackend.asset.map.application.service;

import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.asset.map.application.dto.DetailDTO;
import org.block.blockbackend.asset.map.application.dto.DownloadFileDTO;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.domain.model.Editable;
import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.block.blockbackend.asset.map.domain.service.MapStorageService;
import org.block.blockbackend.asset.map.infrastructure.minio.service.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.block.blockbackend.asset.map.infrastructure.s3.service.S3Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MapApiService {

    private final MapStorageService mapStorageService;

//    private final MinioService minioService;
    private final S3Service s3Service;

    // Minio
//    public MapApiService(MapStorageService mapStoragteService, MinioService minioService) {
//        this.mapStorageService = mapStoragteService;
//        this.minioService = minioService;
//    }

    public MapApiService(MapStorageService mapStoragteService, S3Service s3Service) {
        this.mapStorageService = mapStoragteService;
        this.s3Service = s3Service;
    }

    public void uploadMapFile(List<MultipartFile> images, UploadDTO uploadDTO) throws Exception {

        // Map<String, Object> json, int price, Editable editable, Integer uploader

        Editable editable;
        List<String> imagesURL = new ArrayList<>();

        if ("ABLE".equals(uploadDTO.getEditable())){
            editable = Editable.ABLE;
        } else {
            editable = Editable.UNABLE;
        }

        for (MultipartFile image : images) {
            imagesURL.add(s3Service.uploadFile(image));
        }

        log.info("============Upload imagesURL============\n" + imagesURL);

        mapStorageService.storeMapStorage(
                uploadDTO.getDataTable(),
                uploadDTO.getPrice(),
                uploadDTO.getMapName(),
                imagesURL,
                uploadDTO.getSummary(),
                uploadDTO.getDescription(),
                uploadDTO.getTags(),
                editable,
                uploadDTO.getUploader(),
                uploadDTO.getNumOfPlayer()
        );
    }

    // umap을 저장하는 uploadMapFile 메서드(기존)
//    public void uploadMapFile(MultipartFile file, UploadDTO uploadDTO) throws Exception {
//
//        // Map<String, Object> json, int price, Editable editable, Integer uploader
//
//        Editable editable;
//
//        if ("ABLE".equals(uploadDTO.getEditable())){
//            editable = Editable.ABLE;
//        } else {
//            editable = Editable.UNABLE;
//        }
//
//        String fileName = s3Service.uploadFile(file);
//        mapStorageService.storeMapStorage(
//                fileName,
//                uploadDTO.getDataTable(),
//                uploadDTO.getPrice(),
//                editable,
//                uploadDTO.getUploader());
//    }

    // purchaseMap... 이라고 쓰고 사실 다운로드만 하고 있습니다.
    public DownloadFileDTO purchaseMap(Integer no) throws Exception {
        MapInfo mapInfo = mapStorageService.getMapStorage(no);

        log.info("========== description ============\n" + mapInfo);

        DownloadFileDTO result = new DownloadFileDTO(
                mapInfo.getNo(),
                mapInfo.getDataTable()
        );

        return result;
    }

    public DetailDTO description(Integer no) throws Exception {

        MapInfo mapInfo = mapStorageService.getMapStorage(no);

        DetailDTO result = new DetailDTO(
                mapInfo.getNo(),
                mapInfo.getMapName(),
                mapInfo.getImagesURL(),
                mapInfo.getSummary(),
                mapInfo.getDescription(),
                mapInfo.getTags(),
                mapInfo.getPlayer()
        );

        return result;
    }
//
//    public InputStream downloadMapFile(String fileName) throws Exception {
//
//        InputStream result = s3Service.downloadFile(fileName);
//
//        return result;
//    }

}
