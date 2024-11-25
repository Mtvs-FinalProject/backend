package org.block.blockbackend.asset.map.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "map_info")
@Getter
@NoArgsConstructor
@ToString
public class MapInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false, unique = true)
    private Integer no;

//    @Column(name="umap")
//    private String umap;

    @JdbcTypeCode(SqlTypes.JSON)    // after hibernate 6
    @Column(name="data_table", columnDefinition = "jsonb")
    private List<Map<String, Object>> dataTable;

    @ColumnDefault("0")
    @Column(name="price", nullable = false)
    private int price;

    @Column(name="map_name")
    private String mapName;

    @JdbcTypeCode(SqlTypes.ARRAY)   // 기본 자료형 외에도 db에 저장할 수 있게 해준다.
    @Column(name="images_url", columnDefinition = "text[]")
    private List<String> imagesURL;

    @Column(name="summary")
    private String summary;

    @Column(name="content")
    private String content;

    @JdbcTypeCode(SqlTypes.ARRAY)   // 기본 자료형 외에도 db에 저장할 수 있게 해준다.
    @Column(name="tags", columnDefinition = "text[]")   // 문자열 배열 저장
    private List<String> tags;

    @ColumnDefault("'ABLE'")
    @Enumerated(EnumType.STRING)
    @Column(name="editable", nullable = false)
    private Editable editable;

    @Column(name="uploader")
    private Long uploader;

    @Column(name="player")
    private int player;

    @Column(name="create_at")
    private Timestamp createAt;

    public MapInfo(List<Map<String, Object>> dataTable, int price, String mapName, List<String> imagesURL, String summary, String description, List<String> tags, Editable editable, Long uploader, int player, Timestamp createAt) {
        this.dataTable = dataTable;
        this.price = price;
        this.mapName = mapName;
        this.imagesURL = imagesURL;
        this.summary = summary;
        this.content = description;
        this.tags = tags;
        this.editable = editable;
        this.uploader = uploader;
        this.player = player;
        this.createAt = createAt;
    }
}
