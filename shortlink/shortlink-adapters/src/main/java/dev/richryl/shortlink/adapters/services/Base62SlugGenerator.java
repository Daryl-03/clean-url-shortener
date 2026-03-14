package dev.richryl.shortlink.adapters.services;

import dev.richryl.shortlink.application.exceptions.SlugGenerationException;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

public class Base62SlugGenerator implements SlugGenerator {
    @Override
    public String generate(String input) {
        String shortCode;
        if(input == null || input.isEmpty()) {
            throw new SlugGenerationException("Input URL cannot be null or empty.");
        }
        byte[] clippedHashedUrl = getPortionOfBytesFromHashedUrl(input);
        shortCode = Base62.encode(clippedHashedUrl);
        return shortCode;
    }

    private static byte[] getPortionOfBytesFromHashedUrl(String url) {
        // add random salt to the URL to ensure uniqueness
        url += UUID.randomUUID().toString();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new SlugGenerationException("Hashing algorithm not found on the platform.", e);
        }
        byte[] hashedUrl = md.digest(url.split("://")[1].getBytes()); // hash the URL using MD5
        return Arrays.copyOf(hashedUrl, 8);
    }
}
