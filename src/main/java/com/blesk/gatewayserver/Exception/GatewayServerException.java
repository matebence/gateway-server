package com.blesk.gatewayserver.Exception;

import org.springframework.http.HttpStatus;

public class GatewayServerException extends RuntimeException {

    private HttpStatus httpStatus;

    public GatewayServerException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}