package dev.richryl.shortlink.shortlink_application_impl;

import dev.richryl.shortlink.shortlink_application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.shortlink_application.ports.out.SlugGenerator;
import dev.richryl.shortlink.shortlink_domain.Shortlink;

import java.util.regex.Pattern;

public class CreateShortlinkInteractor implements CreateShortlinkUseCase {

    private final SlugGenerator slugGenerator;

    public CreateShortlinkInteractor(SlugGenerator slugGenerator) {
        this.slugGenerator = slugGenerator;
    }

    @Override
    public Shortlink handle(String url) {
        Pattern validUrl = Pattern.compile("^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$");
        if (!validUrl.matcher(url).find()){
            throw new IllegalArgumentException("The url is not valid.");
        }
        return new Shortlink(url, slugGenerator.generate(url));
    }


}
