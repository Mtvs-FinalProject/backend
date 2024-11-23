package org.block.blockbackend.core.error;

import lombok.Getter;
import java.sql.Timestamp;

@Getter
public class ApplicationException extends RuntimeException {

    ErrorCode errorCode;
    Timestamp timestamp;

    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
