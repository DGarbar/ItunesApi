package com.dharbar.template.service.musicresources.itunes.openapi;

import com.dharbar.template.service.musicresources.itunes.openapi.dto.openapi.ItunesResponse;
import com.dharbar.template.service.musicresources.itunes.openapi.dto.openapi.ItunesResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Service
public class OpenApiItunesRequester {

    private final URIBuilder searchUriBuilder = new URIBuilder()
            .setScheme("https")
            .setHost("itunes.apple.com")
            .setPath("/search");

    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    public OpenApiItunesRequester(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    public Flux<ItunesResult> request(List<NameValuePair> params) {
        return requestLimited(params);
    }

    public Mono<ItunesResult> requestOne(List<NameValuePair> params) {
        return requestLimited(params).singleOrEmpty();
    }

    private Flux<ItunesResult> requestLimited(List<NameValuePair> params) {
        try {
            URI searchUrl = searchUriBuilder
//                    .addParameter("limit", String.valueOf(limit))
                    .addParameters(params)
                    .build();
            log.info("sending to Itunes uri {}", searchUrl.toString());
            return webClientBuilder.build().get()
                    .uri(searchUrl)
                    .accept(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE))
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMapMany(this::getItunesResponse);
        } catch (URISyntaxException e) {
            return Flux.error(e);
        }
    }

    private Flux<ItunesResult> getItunesResponse(String rawItunesResponse) {
        try {
            final ItunesResponse itunesResponse = objectMapper.readValue(rawItunesResponse, ItunesResponse.class);
            final List<ItunesResult> itunesResults = itunesResponse.getResults();
            return Flux.fromIterable(itunesResults);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cant deserialize: " + rawItunesResponse, e);
        }
    }
}
