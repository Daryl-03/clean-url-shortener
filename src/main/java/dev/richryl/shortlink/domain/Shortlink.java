package dev.richryl.shortlink.shortlink_domain;

public class Shortlink {
    private final String originalUrl;
    private final String shortCode;

    public Shortlink(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }
}
