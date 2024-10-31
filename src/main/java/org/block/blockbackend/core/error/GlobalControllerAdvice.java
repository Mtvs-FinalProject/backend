package org.block.blockbackend.core.error;

import lombok.extern.slf4j.Slf4j;
import org.block.blockbackend.core.utils.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity handleApplicationException(ApplicationException e) {
        log.error("Error occurs {}", e.toString());

        Map<String,Object> data = new HashMap<>();
        data.put("status",e.getErrorCode().getStatus().value());
        data.put("errorCode", e.getErrorCode().getErrorCode());
        data.put("message",e.getErrorCode().getMessage());
        data.put("timestamp", e.getTimestamp());

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ApiUtils.error(data));
    }
}
