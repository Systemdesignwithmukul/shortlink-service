package com.shortlink.controller;

import com.riskplatform.transaction_service.api.ShortlinkApi;
import com.riskplatform.transaction_service.dto.CreateShortUrlRequest;
import com.riskplatform.transaction_service.dto.CreateShortUrlResponse;
import com.riskplatform.transaction_service.dto.ExpandShortUrlResponse;
import com.shortlink.service.ShortlinksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortlinksController implements ShortlinkApi {
    private final ShortlinksService shortlinksService;

    @Override
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(CreateShortUrlRequest createShortUrlRequest) {
        return ResponseEntity.ok(shortlinksService.shortenUrl(createShortUrlRequest));
    }

    @Override
    public ResponseEntity<ExpandShortUrlResponse> expandShortUrl(String shortCode) {
        return null;
    }
}
