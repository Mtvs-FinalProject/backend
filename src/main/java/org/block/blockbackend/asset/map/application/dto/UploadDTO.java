package org.block.blockbackend.asset.map.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UploadDTO {

    @JsonProperty("data_table")
    private Map<String, Object> dataTable;
    private int price;
    private String editable;
    private Integer uploader;
}
