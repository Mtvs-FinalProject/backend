package org.block.blockbackend.asset.map.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.checkerframework.checker.mustcall.qual.NotOwning;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DownloadFileDTO {

    @JsonProperty("map_no")
    private Integer mapNo;

    // Json
    @JsonProperty("data_table")
    private Map<String, Object> dataTable;

}
