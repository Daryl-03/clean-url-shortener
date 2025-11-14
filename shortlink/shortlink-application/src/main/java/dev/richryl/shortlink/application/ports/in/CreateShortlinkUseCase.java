package dev.richryl.shortlink.application.ports.in;


import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;

public interface CreateShortlinkUseCase {

    ShortlinkResponse handle(String url);

}