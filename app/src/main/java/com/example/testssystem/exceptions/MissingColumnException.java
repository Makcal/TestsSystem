package com.example.testssystem.exceptions;

public class MissingColumnException extends Exception {
    public MissingColumnException(Exception e) {
        super(e);
    }
}
