package com.example.urlshortener.service;

import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final ShortUrlRepository repo;
    private final Random random = new Random();

    public UrlShortenerService(ShortUrlRepository repo) {
        this.repo = repo;
    }

    // Generate random 6-character short code
    private String generateCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // CREATE STANDARD SHORT URL
    public ShortUrl createShortUrl(String longUrl) {
        String code = generateCode();

        ShortUrl s = new ShortUrl();
        s.setOriginalUrl(longUrl);
        s.setShortCode(code);
        s.setClicks(0);
        s.setCreatedAt(LocalDateTime.now());

        return repo.save(s);
    }

    // CREATE CUSTOM SHORT URL
    // RULE: If alias exists → return the existing one
    public ShortUrl createCustomUrl(String longUrl, String customCode) {

        Optional<ShortUrl> existing = repo.findByShortCode(customCode);

        if (existing.isPresent()) {
            // Alias already exists → return existing
            return existing.get();
        }

        ShortUrl s = new ShortUrl();
        s.setOriginalUrl(longUrl);
        s.setShortCode(customCode);
        s.setClicks(0);
        s.setCreatedAt(LocalDateTime.now());

        return repo.save(s);
    }

    // GET SHORT URL
    public ShortUrl getByCode(String code) {
        return repo.findByShortCode(code)
                .orElseThrow(() -> new RuntimeException("Code not found"));
    }

    // INCREMENT CLICKS
    public void incrementClicks(ShortUrl s) {
        s.setClicks(s.getClicks() + 1);
        repo.save(s);
    }

    // UPDATE URL
    public ShortUrl update(String code, String newUrl) {
        ShortUrl s = getByCode(code);
        s.setOriginalUrl(newUrl);
        return repo.save(s);
    }

    // DELETE URL
    public void deleteByCode(String code) {
        ShortUrl s = getByCode(code);
        repo.delete(s);
    }
}
