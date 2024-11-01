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
import org.block.blockbackend.user.entity.User;
import org.block.blockbackend.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                    .body(UserResponseBody.signupError());
        }
        userService.registerUser(signUpDTO);
        return ResponseEntity.ok().body(UserResponseBody.signupSuccess());
    }

    @Operation(summary = "회원 탈퇴 api", description = "회원 탈퇴하는 기능입니다.")
    @DeleteMapping("remove")
    public ResponseEntity<?> remove(@UserIdFromToken UserId userId) {
        log.info("userNo: {}", userId);
        try{
            userService.removeUser(userId.getUserId());
            return ResponseEntity
                    .ok()
                    .body(UserResponseBody.removeSuccess());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(UserResponseBody.removeError());
        }
    }

    @Operation(summary = "로그인 api", description = "회원 로그인 기능입니다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(UserResponseBody.loginError());
        }
        try{
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + userService.login(loginDTO))
                    .body(UserResponseBody.loginSuccess());
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(UserResponseBody.loginError());
        }
    }

    @Operation(summary = "회원 전체 조회 api", description = "개발용으로 회원의 전체 정보를 반환합니다")
    @GetMapping
    public ResponseEntity<List<User>>  getAllUserData() {
        return ResponseEntity.ok().body(userService.getUser());
    }
}
