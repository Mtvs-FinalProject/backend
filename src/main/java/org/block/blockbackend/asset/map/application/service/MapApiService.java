package org.block.blockbackend.asset.map.application.service;

import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.asset.map.application.dto.DetailDTO;
import org.block.blockbackend.asset.map.application.dto.DownloadFileDTO;
import org.block.blockbackend.asset.map.application.dto.UploadDTO;
import org.block.blockbackend.asset.map.domain.model.Editable;
import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.block.blockbackend.asset.map.domain.service.MapStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.block.blockbackend.asset.map.infrastructure.minio.service.S3Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MapApiService {

    private final MapStorageService mapStorageService;
    private final S3Service s3Service;

    public MapApiService(MapStorageService mapStoragteService, S3Service s3Service) {
        this.mapStorageService = mapStoragteService;
        this.s3Service = s3Service;
    }

    public MapInfo findMapInfoByNo(Integer no){
        return mapStorageService.findMapInfoByNo(no);
    }

    public List<Map<String, Object>> getMapList() {

        List<MapInfo> maps = mapStorageService.getMapStorageList();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (MapInfo map : maps) {
            Map<String, Object> mapInfo = new HashMap<>();
            mapInfo.put("no", map.getNo());
            mapInfo.put("mapName", map.getMapName());
            mapInfo.put("price", map.getPrice());
            mapInfo.put("summary", map.getSummary());
            mapList.add(mapInfo);
        }
        return mapList;
    }

    public MapInfo uploadMapFile(Long userId,List<MultipartFile> images, UploadDTO uploadDTO) throws Exception {
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

        return mapStorageService.storeMapStorage(
                uploadDTO.getDataTable(),
                uploadDTO.getPrice(),
                uploadDTO.getMapName(),
                imagesURL,
                uploadDTO.getSummary(),
                uploadDTO.getContent(),
                uploadDTO.getTags(),
                editable,
                userId,
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
                mapInfo.getContent(),
                mapInfo.getTags(),
                mapInfo.getPlayer(),
                mapInfo.getCreateAt());
        return result;
    }

    public MapInfo findMapInfoByMapName(String name) {
        return mapStorageService.findMapInfoByMapName(name);
    }

//
//    public InputStream downloadMapFile(String fileName) throws Exception {
//
//        InputStream result = s3Service.downloadFile(fileName);
//
//        return result;
//    }

}
