package dev.richryl.identity.application.ports.out;

public interface LoggerPort {
    void info(String message);
    void warn(String message);
    void error(String message);
}
