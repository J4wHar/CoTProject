package org.eclipse.jakarta.waspsecurity.Exceptions;

public class EmployeeNotAuthorizedException extends RuntimeException{
    String message;
    public EmployeeNotAuthorizedException(String message) {
        super(message);
        this.message = message;
    }
}
