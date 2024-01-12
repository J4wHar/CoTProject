package com.waspsecurity.waspsecurity.Exceptions;

public class LightingModuleNotFoundException extends RuntimeException{
    String message;
    public LightingModuleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
