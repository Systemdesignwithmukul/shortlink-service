package com.shortlink.service.impl;

import com.riskplatform.transaction_service.dto.CreateShortUrlRequest;
import com.riskplatform.transaction_service.dto.CreateShortUrlResponse;
import com.shortlink.entity.Shortlinks;
import com.shortlink.exception.HashGenerationApiException;
import com.shortlink.exception.ShortUrlException;
import com.shortlink.repository.ShortlinkRepository;
import com.shortlink.service.ShortlinksService;
import com.shortlink.util.Base62;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class ShortlinksServiceImpl implements ShortlinksService {
    private final ShortlinkRepository shortlinkRepository;
    private String BASE_URL = "http://localhost:8080/";

    @Override
    public CreateShortUrlResponse shortenUrl(CreateShortUrlRequest createShortUrlRequest) {
        String originalUrl = String.valueOf(createShortUrlRequest.getOriginalUrl());

        if (!StringUtils.hasText(originalUrl)) {
            throw new ShortUrlException(
                    HttpStatus.BAD_REQUEST,"Original URL cannot be empty or contain only whitespaces");
        }
        var urlHash = generateHash(originalUrl);
        return shortlinkRepository
                .findByOriginalUrlHash(urlHash)
                .map(
                        existingShortlink ->
                                handleExistingShortlink(createShortUrlRequest, urlHash, existingShortlink))
                .orElseGet(() -> createNewShortlink(createShortUrlRequest, urlHash));

    }

    private CreateShortUrlResponse handleExistingShortlink(
            CreateShortUrlRequest request, String urlHash, Shortlinks existingShortlink) {
        if (isDuplicate(request, urlHash, existingShortlink)) {
            return updateAndReturnExistingShortCode(existingShortlink);
        }
        // If it not a duplicate, it means we have a hash collision.
        throw new HashGenerationApiException(
                "Generated sha256hex Hash has a collision,Unable to generate shorturl",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private CreateShortUrlResponse updateAndReturnExistingShortCode(Shortlinks shortlinks) {
        shortlinks.setUpdatedAt(Instant.now());
        shortlinks.setExpirationDate(Instant.now().plus(30, ChronoUnit.DAYS));
        shortlinkRepository.save(shortlinks);
        return new CreateShortUrlResponse()
                .shortUrl(URI.create(BASE_URL + shortlinks.getShortCode()));
    }

    private CreateShortUrlResponse createNewShortlink(
            CreateShortUrlRequest createShortUrlRequest, String urlHash) {
        Shortlinks shortlinks =
                Shortlinks.builder()
                        .originalUrl(String.valueOf(createShortUrlRequest.getOriginalUrl()))
                        .originalUrlHash(urlHash)
                        .build();

        shortlinks = shortlinkRepository.save(shortlinks);

        // Short code or Encoding for id=1 will be "1", so that's why added 1 million.
        var shortCode = Base62.encode(shortlinks.getId() + 1_000_000);
        var fullShortUrl = BASE_URL + shortCode;
        shortlinks.setShortCode(shortCode);

        shortlinks.setExpirationDate(Instant.now().plus(30, ChronoUnit.DAYS));
        shortlinkRepository.save(shortlinks);

        return new CreateShortUrlResponse().shortUrl(URI.create(fullShortUrl));
    }

    private String generateHash(String originalUrl) {
        return DigestUtils.sha256Hex(originalUrl);
    }

    private boolean isDuplicate(
            CreateShortUrlRequest createShortUrlRequest, String urlHash, Shortlinks shortlinks) {
        return shortlinks.getOriginalUrlHash().equals(urlHash)
                && shortlinks.getOriginalUrl().equals(String.valueOf(createShortUrlRequest.getOriginalUrl()));
    }
}
