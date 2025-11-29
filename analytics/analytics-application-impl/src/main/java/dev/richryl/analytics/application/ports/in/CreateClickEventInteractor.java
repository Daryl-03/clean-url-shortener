package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.dto.CreateClickEventCommand;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;
import dev.richryl.identity.application.ports.out.ClickEventIdGenerator;
import dev.richryl.identity.application.ports.out.ClickEventRepository;
import dev.richryl.identity.application.ports.out.DeviceInfoParser;
import dev.richryl.identity.application.ports.out.GeoLocationProvider;

import java.time.Instant;
import java.util.UUID;

public class CreateClickEventInteractor implements CreateClickEventUseCase {
    private final ClickEventRepository clickEventRepository;
    private final ClickEventIdGenerator clickEventIdGenerator;
    private final DeviceInfoParser deviceInfoParser;
    private  final GeoLocationProvider geoLocationProvider;

    public CreateClickEventInteractor(ClickEventRepository clickEventRepository, ClickEventIdGenerator clickEventIdGenerator, DeviceInfoParser deviceInfoParser, GeoLocationProvider geoLocationProvider) {
        this.clickEventRepository = clickEventRepository;
        this.clickEventIdGenerator = clickEventIdGenerator;
        this.deviceInfoParser = deviceInfoParser;
        this.geoLocationProvider = geoLocationProvider;
    }


    @Override
    public void handle(CreateClickEventCommand command) {
        UUID clickEventId = clickEventIdGenerator.generate();
        DeviceInfo deviceInfo = deviceInfoParser.createDeviceInfo(command.userAgent());
        GeoLocation geoLocation = geoLocationProvider.getGeoLocation(command.ipAddress());
        ClickEvent clickEvent = new ClickEvent(
                clickEventId,
                Instant.now(),
                command.shortlinkId(),
                command.referrer(),
                geoLocation,
                deviceInfo
        );
        clickEventRepository.save(clickEvent);
    }
}
