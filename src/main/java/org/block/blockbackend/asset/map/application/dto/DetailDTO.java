package org.block.blockbackend.asset.map.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DetailDTO {

    @JsonProperty("map_no")
    private Integer mapNo;

    @JsonProperty("map_name")
    private String mapName;

    @JsonProperty("images_url")
    private List<String> imagesURL;

    private String summary;

    private String description;

    private List<String> tags;

    private int player;

}