package org.example.tttn.exception;

import org.example.tttn.constants.MessageCode;

/**
 * Lớp đại diện cho các lỗi trong API
 */
public class ApiException extends RuntimeException {
    private final int status;
    private final MessageCode messageCode;

    public ApiException(MessageCode messageCode) {
        super(messageCode.getMessage());
        this.status = 400;
        this.messageCode = messageCode;
    }

    public ApiException(int status, MessageCode messageCode) {
        super(messageCode.getMessage());
        this.status = status;
        this.messageCode = messageCode;
    }

    public ApiException(int status, MessageCode messageCode, String customMessage) {
        super(customMessage);
        this.status = status;
        this.messageCode = messageCode;
    }

    public int getStatus() {
        return status;
    }

    public MessageCode getMessageCode() {
        return messageCode;
    }
}
