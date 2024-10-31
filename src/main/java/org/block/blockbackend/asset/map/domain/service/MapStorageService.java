package org.block.blockbackend.asset.map.domain.service;

import org.block.blockbackend.asset.map.domain.model.Editable;
import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.block.blockbackend.asset.map.domain.repository.MapStorageRepository;
import org.block.blockbackend.core.error.ApplicationException;
import org.block.blockbackend.core.error.ErrorCode;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class MapStorageService {

    private MapStorageRepository mapStorageRepository;

    public MapStorageService(MapStorageRepository mapStorageRepository) {
        this.mapStorageRepository = mapStorageRepository;
    }

    public void storeMapStorage(String mapName, List<Map<String, Object>> json, Integer price, Editable editable, Integer uploader) {
        MapInfo mapInfo = new MapInfo(mapName, json, price, editable, uploader, new Timestamp(System.currentTimeMillis()));
        mapStorageRepository.save(mapInfo);
    }

    public MapInfo getMapStorage(Integer no) {
        return mapStorageRepository.findById(no).orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
    }

    public MapInfo getMapStorage(String mapName) {
        return mapStorageRepository.findMapInfoByUmap(mapName);
    }
}
