package com.example.hotel.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
