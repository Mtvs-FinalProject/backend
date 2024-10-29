package org.block.blockbackend.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String passwd;
}
