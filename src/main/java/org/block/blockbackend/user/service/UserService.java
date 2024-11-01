package org.block.blockbackend.user.service;

import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.user.dto.LoginDTO;
import org.block.blockbackend.user.dto.SignUpDTO;
import org.block.blockbackend.user.entity.Role;
import org.block.blockbackend.user.entity.User;
import org.block.blockbackend.user.repository.UserRepository;
import org.block.blockbackend.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserService {

    public UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User registerUser(SignUpDTO signUpDTO) {

        User user = new User(
                signUpDTO.getId(),
                signUpDTO.getPasswd(),
                signUpDTO.getName(),
                BigDecimal.valueOf(3000),
                Role.USER
        );

        return userRepository.save(user);
    }

    public void removeUser(Long userNo) {
        userRepository.deleteById(userNo);
    }

    @Transactional
    public String login(LoginDTO loginDTO) {
        User user = userRepository.findById(loginDTO.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!user.getPasswd().equals(loginDTO.getPasswd())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        try{
            user.setLastConnectTime(LocalDateTime.now());
            userRepository.save(user);
            String token = jwtTokenProvider.createToken(user.getNo());
            log.info("user.getNo(): {}", user.getNo());
            log.info("token: {}", token);
            return token;
        } catch (Exception e) {
            throw new IllegalArgumentException("로그인에 실패하였습니다.");
        }
    }

    public List<User> getUser() {
        return userRepository.findAll();
    }
}
