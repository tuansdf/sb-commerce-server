package com.example.sbt.common.exception;

public class PaymentException extends Exception {

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(Exception e) {
        super(e);
    }

}
