package org.block.blockbackend.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.block.blockbackend.domain.user.dto.SignUpDTO;
import org.block.blockbackend.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원가입 api", description = "회원을 등록하는 기능입니다.")
    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody SignUpDTO signUpDTO) {
        if (signUpDTO.getId() == null || signUpDTO.getPasswd() == null || signUpDTO.getName() == null) {
            return ResponseEntity.badRequest().build();
        }
        userService.registerUser(signUpDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "회원 삭제 api", description = "회원을 삭제하는 기능입니다.")
    @DeleteMapping("remove")
    public ResponseEntity<?> remove(@RequestParam Integer id) {
        userService.removeUser(id);
        return ResponseEntity.ok().build();
    }
}
