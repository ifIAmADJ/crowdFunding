package com.iproject.crowd.exception;

/**
 * 当用户注册新账号时检测到该登录账号重复，则抛出此异常。
 * @author liJunhu
 */
public class AcctNotUniqueException  extends RuntimeException{

    public AcctNotUniqueException() {
    }

    public AcctNotUniqueException(String message) {
        super(message);
    }

    public AcctNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcctNotUniqueException(Throwable cause) {
        super(cause);
    }

    public AcctNotUniqueException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
