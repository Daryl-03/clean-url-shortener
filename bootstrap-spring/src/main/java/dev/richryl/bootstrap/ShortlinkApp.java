package dev.richryl.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"dev.richryl.*", "dev.richryl.bootstrap"})
@SpringBootApplication
public class ShortlinkApp {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApp.class, args);
    }
}
