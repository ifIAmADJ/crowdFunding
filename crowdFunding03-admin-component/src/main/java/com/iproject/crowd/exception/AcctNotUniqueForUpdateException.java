package com.iproject.crowd.exception;

/**
 * 当用户注册新账号时检测到该登录账号重复，则抛出此异常。
 * @author liJunhu
 */
public class AcctNotUniqueForUpdateException extends RuntimeException{

    public AcctNotUniqueForUpdateException() {
    }

    public AcctNotUniqueForUpdateException(String message) {
        super(message);
    }

    public AcctNotUniqueForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AcctNotUniqueForUpdateException(Throwable cause) {
        super(cause);
    }

    public AcctNotUniqueForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
