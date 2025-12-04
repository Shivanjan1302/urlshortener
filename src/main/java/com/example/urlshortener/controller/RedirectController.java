package com.example.urlshortener.controller;

import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class RedirectController {

    private final UrlShortenerService service;

    public RedirectController(UrlShortenerService service) {
        this.service = service;
    }

    // Home page – always load index.html
    @GetMapping("/")
    public String home() {
        return "redirect:/index.html";
    }

    // Redirect only valid short codes (3 to 10 alphanumeric chars)
    @GetMapping("/{code:[A-Za-z0-9]{3,10}}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        ShortUrl s = service.getByCode(code);
        service.incrementClicks(s);

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", s.getOriginalUrl());
    }
}
