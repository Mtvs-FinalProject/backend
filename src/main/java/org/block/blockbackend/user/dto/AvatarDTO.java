package org.block.blockbackend.user.dto;

import lombok.Data;
import org.block.blockbackend.user.entity.Avatar;

@Data
public class AvatarDTO {

    private String rgb;

    public AvatarDTO(Avatar avatar) {
        this.rgb = avatar.getRgb();
    }
}
