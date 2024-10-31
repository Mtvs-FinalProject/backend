package org.block.blockbackend.asset.map.application.service;

import org.block.blockbackend.asset.map.application.dto.DownloadFileDTO;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.domain.model.Editable;
import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.block.blockbackend.asset.map.domain.service.MapStorageService;
import org.block.blockbackend.asset.map.infrastructure.minio.service.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

@Service
public class MapApiService {

    private final MapStorageService mapStorageService;

    private final MinioService minioService;

    public MapApiService(MapStorageService mapStoragteService, MinioService minioService) {
        this.mapStorageService = mapStoragteService;
        this.minioService = minioService;
    }

    public void uploadMapFile(MultipartFile file, UploadDTO uploadDTO) throws Exception {

        // Map<String, Object> json, int price, Editable editable, Integer uploader

        Editable editable;

        if ("ABLE".equals(uploadDTO.getEditable())){
            editable = Editable.ABLE;
        } else {
            editable = Editable.UNABLE;
        }

        String fileName = minioService.uploadFile(file);
        mapStorageService.storeMapStorage(
                fileName,
                uploadDTO.getDataTable(),
                uploadDTO.getPrice(),
                editable,
                uploadDTO.getUploader());
    }

    public DownloadFileDTO purchaseMap(Integer no) throws Exception {
        MapInfo mapInfo = mapStorageService.getMapStorage(no);
        DownloadFileDTO result = new DownloadFileDTO(
                mapInfo.getNo(),
                mapInfo.getUmap(),
                mapInfo.getDataTable()
        );

        return result;
    }
    public InputStream downloadMapFile(String fileName) throws Exception {

        InputStream result = minioService.downloadFile(fileName);

        return result;
    }

}
