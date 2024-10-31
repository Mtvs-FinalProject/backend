package org.block.blockbackend.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.expiration_time}")
    private long expirationTime;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    // @PostConstruct로 Key 초기화
    @PostConstruct
    public void init() {
        // NOTE: UTF_8 코드가 플랫폼에 의존하지 않고 일관된 바이트 배열을 생성하는 이점이 있습니다.
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성 메서드
    public String createToken(Long userId) {

        return Jwts.builder()
                .subject(userId.toString()) // 토큰 제목 설정
                .issuedAt(new Date()) // 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(this.secretKey)  // 서명 키 설정 (알고리즘 자동 설정)
                .compact();
    }

    public Long getUserId(String token) {
        try{
            return Long.parseLong(Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        }catch (JwtException e){
            log.error("Invalid JWT token: " + e.getMessage());
            return null;
        }
    }



}
