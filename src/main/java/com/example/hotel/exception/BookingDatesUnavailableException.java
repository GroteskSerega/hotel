package com.example.hotel.exception;

public class BookingDatesUnavailableException extends RuntimeException {
    public BookingDatesUnavailableException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
