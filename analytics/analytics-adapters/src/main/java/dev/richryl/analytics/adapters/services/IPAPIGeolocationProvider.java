// java
package dev.richryl.analytics.adapters.services;

import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.out.GeoLocationProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class IPAPIGeolocationProvider implements GeoLocationProvider {
    private final String BASE_URL = "http://ip-api.com/json/{}?fields=57555";
    private final RestTemplate restTemplate;
    private final AtomicInteger remainingRequests = new AtomicInteger(45);
    private final int MAX_WAIT_CYCLES = 5;

    public IPAPIGeolocationProvider(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public GeoLocation getGeoLocation(String ipAddress) {
        if(ipAddress == null) {
        System.err.println("Encoded IP: " + ipAddress);
            return GeoLocation.unknown();
        }
        String encodedIp = UriUtils.encodePath(ipAddress, StandardCharsets.UTF_8);
        System.err.println(" IP: " + ipAddress);
        String url = BASE_URL.replace("{}", encodedIp);

        int waitCycles = 0;

        while (waitCycles <= MAX_WAIT_CYCLES) {
            ResponseEntity<IPApiResponse> responseEntity;
            try {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, IPApiResponse.class);
            } catch (Exception e) {
                System.err.println("Error fetching geolocation: " + e.getMessage());
                return null;
            }

            HttpHeaders headers = responseEntity.getHeaders();
            String rlHeader = headers.getFirst("X-Rl");
            String ttlHeader = headers.getFirst("X-Ttl");

            if (rlHeader != null) {
                try {
                    int rl = Integer.parseInt(rlHeader);
                    remainingRequests.set(rl);
                } catch (NumberFormatException ignored) {
                }
            }

            IPApiResponse body = responseEntity.getBody();

            if (remainingRequests.get() > 0) {
                if (body == null || body.status() == null || !"success".equalsIgnoreCase(body.status())) {
                    System.err.println("IPAPI returned unsuccessful status or null body for IP: " + ipAddress);
                    return null;
                }

                remainingRequests.decrementAndGet();
                System.err.println(
                        "IPAPI Response - Country: " + body.country() +
                                ", CountryCode: " + body.countryCode() +
                                ", City: " + body.city()
                );
                return new GeoLocation(
                        body.countryCode(),
                        body.country(),
                        body.city()
                );
            }

            if (ttlHeader != null) {
                try {
                    long ttlSeconds = Long.parseLong(ttlHeader);
                    if (ttlSeconds > 0) {
                        try {
                            TimeUnit.SECONDS.sleep(ttlSeconds);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                        waitCycles++;
                        continue;
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            // Si pas de header TTL ou valeur invalide, sortir
            return null;
        }

        // Trop de cycles d'attente
        return null;
    }

    record IPApiResponse(
            String query,
            String status,
            String country,
            String countryCode,
            String city,
            double lat,
            double lon
    ) {
    }
}