package com.my.framework.exception;

/**
 * @author <a href="mailto:yu.ou@alpha-ess.com">ouyu</a>
 * @date: 2024/02/04 16:03:23
 */
public class EventHandleException extends RuntimeException{
    private static final long serialVersionUID = 561514561544848127L;

    public EventHandleException() {
        super();
    }

    public EventHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventHandleException(String message) {
        super(message);
    }

    public EventHandleException(Throwable cause) {
        super(cause);
    }
}
