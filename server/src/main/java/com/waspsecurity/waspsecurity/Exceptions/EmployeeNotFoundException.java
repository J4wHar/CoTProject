package com.waspsecurity.waspsecurity.Exceptions;

public class EmployeeNotFoundException extends RuntimeException{
    String message;
    public EmployeeNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
