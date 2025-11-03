package dev.richryl.shortlink.application.exceptions;

public class ShortlinkNotFoundException extends RuntimeException {
    public ShortlinkNotFoundException(String message) {
        super(message);
    }

    public ShortlinkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
