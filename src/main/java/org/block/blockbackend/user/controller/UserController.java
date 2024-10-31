package org.block.blockbackend.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.core.config.UserIdFromToken;
import org.block.blockbackend.user.dto.LoginDTO;
import org.block.blockbackend.user.dto.SignUpDTO;
import org.block.blockbackend.user.dto.UserId;
import org.block.blockbackend.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "회원 API", description = "회원 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 api", description = "회원을 등록하는 기능입니다.")
    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("가입 정보가 올바르지 않습니다.");
        }
        userService.registerUser(signUpDTO);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @Operation(summary = "회원 탈퇴 api", description = "회원 탈퇴하는 기능입니다.")
    @DeleteMapping("remove")
    public ResponseEntity<?> remove(@UserIdFromToken UserId userId) {
        log.info("userNo: {}", userId);
        userService.removeUser(userId.getUserId());
        return ResponseEntity
                .ok()
                .body("회원 탈퇴되었습니다.");
    }

    @Operation(summary = "로그인 api", description = "회원 로그인 기능입니다.")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 정보가 올바르지 않습니다.");
        }
        try{
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + userService.login(loginDTO))
                    .body("로그인 성공하였습니다");
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인 정보가 올바르지 않습니다.");
        }
    }
}
