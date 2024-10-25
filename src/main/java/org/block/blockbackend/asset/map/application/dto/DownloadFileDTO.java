package org.block.blockbackend.asset.map.application.dto;

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

    private Integer mapNo;
    private String fileName;
    // Json
    private List<Map<String, Object>> dataTable;

}
