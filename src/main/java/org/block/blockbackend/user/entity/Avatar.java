package org.block.blockbackend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private Long userId;

    private String rgb = "255,255,255";

    public Avatar(Long userId, String rgb) {
        this.userId = userId;
        this.rgb = rgb;
    }
}
