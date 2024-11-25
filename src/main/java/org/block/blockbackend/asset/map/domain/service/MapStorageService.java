package org.block.blockbackend.asset.map.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.asset.map.domain.model.Editable;
import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.block.blockbackend.asset.map.domain.repository.MapStorageRepository;
import org.block.blockbackend.core.error.ApplicationException;
import org.block.blockbackend.core.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MapStorageService {

    private MapStorageRepository mapStorageRepository;

    public MapStorageService(MapStorageRepository mapStorageRepository) {
        this.mapStorageRepository = mapStorageRepository;
    }

//    public Pageable<MapInfo>

    @Transactional
    public void storeMapStorage(List<Map<String, Object>> json, Integer price, String mapName, List<String> imagesURL, String summary, String content, List<String> tags, Editable editable, Long uploader, int player) {
        MapInfo mapInfo = new MapInfo(json, price, mapName, imagesURL, summary, content, tags, editable, uploader, player, new Timestamp(System.currentTimeMillis()));

        log.info("mapInfo: {}", mapInfo);
        mapStorageRepository.save(mapInfo);
        log.info("mapInfo: {}", mapInfo);
    }

    // umap 저장 하는 service
//    public void storeMapStorage(String mapName, List<Map<String, Object>> json, Integer price, Editable editable, Integer uploader) {
//        MapInfo mapInfo = new MapInfo(mapName, json, price, editable, uploader, new Timestamp(System.currentTimeMillis()));
//        mapStorageRepository.save(mapInfo);
//    }

    public MapInfo getMapStorage(Integer no) {

        MapInfo mapInfo = null;

        mapInfo = mapStorageRepository.findById(no).orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));

        log.info("getMapStorage: mapInfo={}", mapInfo);
//        return mapStorageRepository.findById(no).orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
        return mapInfo;
    }

//    public MapInfo getMapStorage(String mapName) {
//        return mapStorageRepository.findMapInfoByUmap(mapName);
//    }

    public MapInfo getMapStorageByMapName(String mapName) {
        return mapStorageRepository.findMapInfoByMapName(mapName);
    }

    public MapInfo getAllMapStorage() {
        return mapStorageRepository.findAll().stream().findFirst().get();
    }

    public List<MapInfo> getMapStorageList() {
        return mapStorageRepository.findAll();
    }
}
