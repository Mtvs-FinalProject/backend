package org.block.blockbackend.asset.map.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopListDTO {

    @JsonProperty("map_no")
    private String mapNo;

    private String mapName;

    private String player;

}
