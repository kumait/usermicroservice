package io.primecoders.voctrainer.userservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @GetMapping("ping")
    public String ping() {
        return "OK";
    }

    @GetMapping("profile")
    public String profile() {
        return activeProfile;
    }
}
