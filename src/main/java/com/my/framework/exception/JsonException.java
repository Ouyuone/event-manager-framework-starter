package com.my.framework.exception;

import lombok.Getter;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/05 11:34:33
 */
public class JsonException extends RuntimeException{

    @Getter
    private String message;

    public JsonException() {
        super();
    }

    public JsonException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
