package dev.richryl.shortlink.adapaters.services;

import dev.richryl.shortlink.application.exceptions.SlugGenerationException;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class SlugGeneratorTest {

    private final SlugGenerator generator = new Base62SlugGenerator();

    @ParameterizedTest
    @ValueSource(strings = {
            "https://example.com/path?query=1",
            "http://another-example.org/",  
    })
    @DisplayName("generate returns non-null, non-empty, valid string")
    void generateReturnsNonNullNonEmptyString(String url) {
//        String url = "https://example.com/path?query=1";
        String slug = generator.generate(url);
        assertNotNull(slug);
        assertFalse(slug.isEmpty());
        assertFalse(slug.contains(" "));
    }

    @Test
    @DisplayName("generate exception for null or empty input")
    void generateExceptionForNullOrEmptyInput() {
        assertThrows(SlugGenerationException.class, () -> generator.generate(null));
        assertThrows(SlugGenerationException.class, () -> generator.generate(""));
    }

    @Test
    @DisplayName("generate is deterministic for same input")
    void deterministicForSameInput() {
        String url = "https://example.com/path?query=1";
        String s1 = generator.generate(url);
        String s2 = generator.generate(url);
        assertNotNull(s1);
        assertEquals(s1, s2);
    }

    @Test
    @DisplayName("different inputs produce different slugs (very likely)")
    void differentInputsProduceDifferentSlugs() {
        String a = "https://example.com/one";
        String b = "https://example.com/two";
        String sa = generator.generate(a);
        String sb = generator.generate(b);
        assertNotNull(sa);
        assertNotNull(sb);
        assertNotEquals(sa, sb);
    }

}