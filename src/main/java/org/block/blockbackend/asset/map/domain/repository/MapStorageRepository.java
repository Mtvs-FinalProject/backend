package org.block.blockbackend.asset.map.domain.repository;

import org.block.blockbackend.asset.map.domain.model.MapInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapStorageRepository extends JpaRepository<MapInfo, Integer> {

    MapInfo findMapInfoByUmap(String mapName);
}
