package dev.richryl.common.adapters.services;

import dev.richryl.identity.application.ports.out.LoggerPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLoggerAdapter implements LoggerPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }
}
