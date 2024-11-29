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
public class UploadDTO {

//    @JsonProperty("data_table")
//    private Map<String, Object> dataTable;
    @JsonProperty("data_table")
    private String dataTable;
    private int price;
    @JsonProperty("map_name")
    private String mapName;
    private String summary;
    private String content;
    private String editable;
    private List<String> tags;
    @JsonProperty("num_of_player")
    private int numOfPlayer;
}
