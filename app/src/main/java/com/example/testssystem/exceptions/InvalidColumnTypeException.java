package com.example.testssystem.exceptions;

public class InvalidColumnTypeException extends Exception {
    public InvalidColumnTypeException(Exception e) {
        super(e);
    }
}
