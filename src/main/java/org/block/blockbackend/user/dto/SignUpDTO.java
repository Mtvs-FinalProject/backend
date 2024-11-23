package org.block.blockbackend.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요")
//            @Pattern(regexp = "^(?=.*\\d).{6,}$", message = "비밀번호는 최소 6자 이상이어야 하며, 숫자를 포함해야 합니다.")
    private String passwd;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String name;

    @Schema(defaultValue = "255,255,255")
    private String rgb;
}
