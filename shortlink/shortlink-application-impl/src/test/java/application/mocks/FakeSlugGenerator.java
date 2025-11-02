package application.mocks;

import dev.richryl.shortlink.application.ports.out.SlugGenerator;

public class FakeSlugGenerator implements SlugGenerator {


    @Override
    public String generate(String input) {
        return input.substring(Math.max(input.length()-5, 0)) + 2;
    }
}
