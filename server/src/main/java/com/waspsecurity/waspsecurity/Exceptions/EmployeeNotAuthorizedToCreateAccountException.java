package com.waspsecurity.waspsecurity.Exceptions;

public class EmployeeNotAuthorizedToCreateAccountException extends RuntimeException{
    String message;
    public EmployeeNotAuthorizedToCreateAccountException(String message) {
        super(message);
        this.message = message;
    }
}
