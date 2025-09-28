package application.mocks;

import dev.richryl.shortlink.application.ports.out.SlugGenerator;

public class FakeSlugGenerator implements SlugGenerator {
    final String fixedSlug;

    public FakeSlugGenerator(String fixedSlug) {
        this.fixedSlug = fixedSlug;
    }

    @Override
    public String generate(String input) {
        return fixedSlug;
    }
}
