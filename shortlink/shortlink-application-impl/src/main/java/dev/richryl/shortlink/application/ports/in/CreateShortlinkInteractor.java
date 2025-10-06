package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;

import java.util.regex.Pattern;

public class CreateShortlinkInteractor implements CreateShortlinkUseCase{
    private final SlugGenerator slugGenerator;

    public CreateShortlinkInteractor(SlugGenerator slugGenerator) {
        this.slugGenerator = slugGenerator;
    }


    public Shortlink handle(String url){
        return new Shortlink(url, slugGenerator.generate(url));
    }



}
