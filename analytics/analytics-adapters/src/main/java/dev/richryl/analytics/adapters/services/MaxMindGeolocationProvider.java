package dev.richryl.analytics.adapters.services;

import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.out.GeoLocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.InetAddress;

public class MaxMindGeolocationProvider implements GeoLocationProvider {

    @Value("${maxmind.account-id}")
    private int accountId;
    @Value("${maxmind.license-key}")
    private String licenseKey;
    private final WebServiceClient client;
    private final Logger logger = LoggerFactory.getLogger(MaxMindGeolocationProvider.class);

    public MaxMindGeolocationProvider() {
        this.client = new WebServiceClient.Builder(accountId, licenseKey)
                .build();
    }

    @Override
    public GeoLocation getGeoLocation(String ipAddress) {

        try {
            InetAddress inetIpAddress;
            inetIpAddress = InetAddress.getByName("128.101.101.101");
            CountryResponse response = client.country(inetIpAddress);
            CityResponse cityResponse = client.city(inetIpAddress);
            City city = cityResponse.city();

            Country country = response.country();
            System.out.println(country.isoCode());
            System.out.println(country.name());
            return new GeoLocation(
                    country.isoCode(),
                    country.name(),
                    city.name()
            );
        } catch (IOException | GeoIp2Exception e) {
            logger.error("Failed to get geolocation for IP: " + ipAddress, e);
            throw new RuntimeException(e);
        }
    }
}
