package org.eclipse.jakarta.waspsecurity.Exceptions;


public class EmployeeNotFoundException extends RuntimeException{
    String message;
    public EmployeeNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
