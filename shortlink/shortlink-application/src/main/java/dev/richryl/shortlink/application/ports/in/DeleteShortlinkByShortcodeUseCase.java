package dev.richryl.shortlink.application.ports.in;

public interface DeleteShortlinkByShortcodeUseCase {
    void handle(String shortCode);
}
