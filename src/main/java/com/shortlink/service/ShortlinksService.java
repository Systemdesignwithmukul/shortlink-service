package com.shortlink.service;

import com.riskplatform.transaction_service.dto.CreateShortUrlRequest;
import com.riskplatform.transaction_service.dto.CreateShortUrlResponse;

public interface ShortlinksService {
    CreateShortUrlResponse shortenUrl(CreateShortUrlRequest createShortUrlRequest);
}
