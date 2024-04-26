package com.gc.easy.sms.core.exception;

public class SendingException extends Exception {
    public SendingException(String message, Throwable cause) {
        super(message, cause);
    }
}