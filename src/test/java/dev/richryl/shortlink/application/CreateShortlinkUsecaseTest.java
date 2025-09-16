package dev.richryl.shortlink.shortlink_application;

import dev.richryl.shortlink.shortlink_application.mocks.FakeSlugGenerator;
import dev.richryl.shortlink.shortlink_application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.shortlink_application.ports.out.SlugGenerator;
import dev.richryl.shortlink.shortlink_application_impl.CreateShortlinkInteractor;
import dev.richryl.shortlink.shortlink_domain.Shortlink;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class CreateShortlinkUsecaseTest {
    private final SlugGenerator slugGenerator = new FakeSlugGenerator("fixedSlug");
    private final CreateShortlinkUseCase createShortlinkUseCase = new CreateShortlinkInteractor(
            slugGenerator
    );

    @Test
    @DisplayName("Should create shortlink when given valid URL")
    void should_create_shortlink_when_given_validUrl() {
        String validUrl = "https://example.com";

        Shortlink shortlink = createShortlinkUseCase.handle(validUrl);
        assert shortlink != null;
        assert shortlink.getOriginalUrl().equals(validUrl);
        assert shortlink.getShortCode() != null && !shortlink.getShortCode().isEmpty();
    }

    @Test
    @DisplayName("Should throw exception when url is not valid")
    void should_throw_exception_when_not_valid_url(){
        String invalidUrl = "invalidONe";
        assertThrows(IllegalArgumentException.class, ()-> createShortlinkUseCase.handle(invalidUrl));

    }

    @Test
    @DisplayName("Should throw exception when SlugGenerator returns null" )
    void should_throw_exception_when_slugGenerator_returns_null() {
        SlugGenerator nullSlugGenerator = new FakeSlugGenerator(null);
        CreateShortlinkUseCase useCaseWithNullSlugGen = new CreateShortlinkInteractor(nullSlugGenerator);
        String validUrl = "https://example.com";
        assertThrows(InternalError.class, () -> useCaseWithNullSlugGen.handle(validUrl));
    }

}
