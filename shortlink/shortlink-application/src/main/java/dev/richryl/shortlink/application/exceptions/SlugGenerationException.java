package dev.richryl.shortlink.application.exceptions;

public class SlugGenerationException extends RuntimeException {
    public SlugGenerationException(String message) {
        super(message);
    }

    public SlugGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
