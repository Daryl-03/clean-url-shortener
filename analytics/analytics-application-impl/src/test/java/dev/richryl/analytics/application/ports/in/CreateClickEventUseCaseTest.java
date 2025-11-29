package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.application.ports.mocks.FakeClickEventRepository;
import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.dto.CreateClickEventCommand;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;
import dev.richryl.identity.application.ports.out.ClickEventIdGenerator;
import dev.richryl.identity.application.ports.out.ClickEventRepository;
import dev.richryl.identity.application.ports.out.DeviceInfoParser;
import dev.richryl.identity.application.ports.out.GeoLocationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateClickEventUseCaseTest {
    private final ClickEventRepository clickEventRepository = new FakeClickEventRepository();
    private CreateClickEventUseCase createClickEventUseCase;
    private final DeviceInfoParser deviceInfoParser = Mockito.mock(DeviceInfoParser.class);
    private final GeoLocationProvider geoLocationProvider = Mockito.mock(GeoLocationProvider.class);

    @BeforeEach
    void setUp() {
        ClickEventIdGenerator idGenerator = UUID::randomUUID;
        Mockito.when(deviceInfoParser.createDeviceInfo(Mockito.anyString())).thenReturn(
                new DeviceInfo("MockedBrowser", "MockedOS", "MockedDevice")
        );
        Mockito.when(geoLocationProvider.getGeoLocation(Mockito.anyString())).thenReturn(
                new GeoLocation("MockedCountry", "MockedRegion", "MockedCity")
        );
        createClickEventUseCase = new CreateClickEventInteractor(clickEventRepository, idGenerator, deviceInfoParser, geoLocationProvider);
    }

    @Test
    @DisplayName("should create a click event for a given shortcode")
    void shouldCreateClickEventForGivenlinkId() {
        UUID shortlinkId = UUID.randomUUID();
        CreateClickEventCommand command = new CreateClickEventCommand(
                shortlinkId,
                "MockedUserAgent",
                "ipaddress",
                "referrer.com",
                "fr"
        );
        createClickEventUseCase.handle(command);

        List<ClickEvent> retrievedEvents = clickEventRepository.findByShortlinkId(shortlinkId);
        assertEquals(1, retrievedEvents.size());
        assertEquals(shortlinkId, retrievedEvents.getFirst().getShortlinkId());
        assertEquals("MockedDevice", retrievedEvents.getFirst().getDevice().deviceType());
        assertEquals("MockedRegion", retrievedEvents.getFirst().getLocation().countryName());

        Mockito.verify(deviceInfoParser, Mockito.times(1)).createDeviceInfo(Mockito.anyString());
        Mockito.verify(geoLocationProvider, Mockito.times(1)).getGeoLocation(Mockito.anyString());
    }
}
