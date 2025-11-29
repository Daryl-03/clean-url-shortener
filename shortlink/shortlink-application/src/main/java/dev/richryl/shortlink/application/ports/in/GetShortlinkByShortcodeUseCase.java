package dev.richryl.shortlink.application.ports.in;


import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;



public interface GetShortlinkByShortcodeUseCase {
    ShortlinkResponse handle(String shortcode) throws ShortlinkNotFoundException;
}
