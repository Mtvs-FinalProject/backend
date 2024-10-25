package org.block.blockbackend.asset.map.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "map_info")
@Getter
@NoArgsConstructor
public class MapInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false, unique = true)
    private Integer no;

    @Column(name="umap")
    private String umap;

    @JdbcTypeCode(SqlTypes.JSON)    // after hibernate 6
    @Column(name="data_table", columnDefinition = "jsonb")
    private List<Map<String, Object>> dataTable;

    @ColumnDefault("0")
    @Column(name="price", nullable = false)
    private int price;

    @ColumnDefault("'ABLE'")
    @Enumerated(EnumType.STRING)
    @Column(name="editable", nullable = false)
    private Editable editable;

    @Column(name="uploader")
    private Integer uploader;

    @Column(name="create_at")
    private Timestamp createAt;

    public MapInfo(String umap, List<Map<String, Object>> dataTable, int price, Editable editable, Integer uploader, Timestamp createAt) {
        this.umap = umap;
        this.dataTable = dataTable;
        this.price = price;
        this.editable = editable;
        this.uploader = uploader;
        this.createAt = createAt;
    }
}
