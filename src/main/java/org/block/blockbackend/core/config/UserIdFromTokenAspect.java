package org.block.blockbackend.core.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.block.blockbackend.domain.user.dto.UserId;
import org.block.blockbackend.jwt.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class UserIdFromTokenAspect {

    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletRequest httpServletRequest;

    @Around("execution(* org.block.blockbackend..*Controller.*(.., @org.block.blockbackend.core.config.UserIdFromToken (*), ..))")
    public Object handleUserIdAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        Long userId = extractUserIdFromToken();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header or token");
        }

        injectUserIdIntoAnnotatedParam(joinPoint, userId);

        return joinPoint.proceed(joinPoint.getArgs());
    }

    private Long extractUserIdFromToken() {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid Authorization header");
            return null;
        }

        String token = authHeader.substring(7); // "Bearer " 부분 제거
        Long userId = jwtTokenProvider.getUserId(token);

        log.info("Extracted userId: {}", userId);
        return userId == null || userId.equals(0L) ? null : userId;
    }

    private void injectUserIdIntoAnnotatedParam(ProceedingJoinPoint joinPoint, Long userId) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof UserId && hasUserIdFromTokenAnnotation(parameterAnnotations[i])) {
                ((UserId) args[i]).setUserId(userId);
                log.info("Injected userId {} into parameter index {}", userId, i);
                break;
            }
        }
    }

    private boolean hasUserIdFromTokenAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof UserIdFromToken) {
                return true;
            }
        }
        return false;
    }
}
