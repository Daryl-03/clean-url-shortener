package application.ports.in;

import application.mocks.FakeSlugGenerator;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkInteractor;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateShortlinkUsecaseTest {
    private final SlugGenerator slugGenerator = new FakeSlugGenerator("fixedSlug");
    private final CreateShortlinkUseCase createShortlinkUseCase = new CreateShortlinkInteractor(
            slugGenerator
    );

    private Shortlink computeShortlink(String url) {
        return createShortlinkUseCase.handle(url);
    }

    @Test
    @DisplayName("Should create shortlink when given valid URL")
    void should_create_shortlink_when_given_validUrl() {
        String validUrl = "https://example.com";

        Shortlink shortlink = computeShortlink(validUrl);
        assert shortlink != null;
        assert shortlink.getOriginalUrl().equals(validUrl);
        assert shortlink.getShortCode() != null && !shortlink.getShortCode().isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when url is not valid")
    void should_throw_exception_when_not_valid_url(){
        assertThrows(IllegalArgumentException.class, ()-> computeShortlink("invalidONe"));
    }

}