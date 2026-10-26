package com.trasexp.presentation.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api")
    public Map<String, String> index() {
        return Map.of("status", "ok", "service", "monetraq-api");
    }
}
