package com.waspsecurity.waspsecurity.Exceptions;

public class AddressNotFoundException extends RuntimeException{
    String message;
    public AddressNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
