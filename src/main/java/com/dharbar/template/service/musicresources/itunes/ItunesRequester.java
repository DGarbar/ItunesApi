package com.dharbar.template.service.musicresources.itunes;

import com.dharbar.template.service.musicresources.itunes.dto.ItunesResponse;
import com.dharbar.template.service.musicresources.itunes.dto.ItunesResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
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
public class ItunesRequester {

    private final URIBuilder searchUriBuilder = new URIBuilder()
            .setScheme("https")
            .setHost("itunes.apple.com")
            .setPath("/search");

    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClientBuilder;

    public ItunesRequester(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    public Flux<ItunesResult> request(List<NameValuePair> params) {
        return requestLimited(params, 20);
    }

    public Flux<ItunesResult> request(List<NameValuePair> params, int limit) {
        return requestLimited(params, limit);
    }

    public Mono<ItunesResult> requestOne(List<NameValuePair> params) {
        return requestLimited(params, 1).singleOrEmpty();
    }

    private Flux<ItunesResult> requestLimited(List<NameValuePair> params, int limit) {
        try {
            URI searchUrl = searchUriBuilder
                    .addParameter("limit", String.valueOf(limit))
                    .addParameters(params)
                    .build();
            log.info("sending to Itunes uri {}", searchUrl.toString());
            return webClientBuilder.build().get()
                    .uri(searchUrl)
                    .accept(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE))
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMapMany(s -> getItunesResponse(s, limit));
        } catch (URISyntaxException e) {
            return Flux.error(e);
        }
    }

    private Flux<ItunesResult> getItunesResponse(String s, Integer limit) {
        try {
            final ItunesResponse itunesResponse = objectMapper.readValue(s, ItunesResponse.class);
            final List<ItunesResult> itunesResults = itunesResponse.getResults();
            return Flux.fromIterable(itunesResults).take(limit);
        } catch (IOException e) {
            throw new IllegalArgumentException("Cant deserialize: " + s, e);
        }
    }
}
