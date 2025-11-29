package dev.richryl.bootstrap;

import dev.richryl.bootstrap.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@ComponentScan(basePackages = {"dev.richryl.*", "dev.richryl.bootstrap"})
@SpringBootApplication
@Import(AppConfig.class)
public class ShortlinkApp {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApp.class, args);
    }
}
