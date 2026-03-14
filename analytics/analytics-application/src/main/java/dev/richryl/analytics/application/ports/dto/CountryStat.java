package dev.richryl.analytics.application.ports.dto;

public record CountryStat(
        String countryName,
        String city,
        int count
) {
}

