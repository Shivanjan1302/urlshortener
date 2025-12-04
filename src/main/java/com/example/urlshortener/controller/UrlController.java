package com.example.urlshortener.controller;

import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.service.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlShortenerService service;

    public UrlController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<ShortUrl> create(@RequestParam String longUrl) {
        return ResponseEntity.ok(service.createShortUrl(longUrl));
    }

    /*
        CUSTOM URL CREATION RULE:
        If alias is already taken → return the EXISTING URL instead of error.
    */
    @PostMapping("/custom")
    public ResponseEntity<?> custom(
            @RequestParam String longUrl,
            @RequestParam String custom) {

        try {
            ShortUrl created = service.createCustomUrl(longUrl, custom);
            return ResponseEntity.ok(created);

        } catch (RuntimeException ex) {

            // If alias exists → return that alias entry (NO ERROR)
            if (ex.getMessage().toLowerCase().contains("exists")) {
                ShortUrl existing = service.getByCode(custom);
                return ResponseEntity.ok(existing);
            }

            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get/{alias}")
    public ResponseEntity<?> getAlias(@PathVariable String alias) {
        ShortUrl s = service.getByCode(alias);
        return ResponseEntity.ok(s);
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        ShortUrl s = service.getByCode(code);
        service.incrementClicks(s);
        response.sendRedirect(s.getOriginalUrl());
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> delete(@PathVariable String code) {
        service.deleteByCode(code);
        return ResponseEntity.ok("Deleted");
    }
}
