package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResponse;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Service
public class DevApiItunesRequestSearcher {

    private final TokenProvider tokenProvider;

    private final WebClient.Builder webClientBuilder;

    public DevApiItunesRequestSearcher(TokenProvider tokenProvider,
                                       WebClient.Builder webClientBuilder) {
        this.tokenProvider = tokenProvider;
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<ItunesResult> request(List<NameValuePair> params) {
        return requestLimited(params);
    }

    public Mono<ItunesResult> requestOne(List<NameValuePair> params) {
        return requestLimited(params);
    }

    // TODO LIMIT
    private Mono<ItunesResult> requestLimited(List<NameValuePair> params) {
        try {
            var searchUrl = new URIBuilder()
                    .setScheme("https")
                    .setHost("api.music.apple.com")
                    .setPath("/v1/catalog/us/search")
//                    .addParameter("limit", String.valueOf(limit))
                    .addParameters(params)
                    .build();
            log.info("sending to Itunes uri {}", searchUrl.toString());

            return webClientBuilder.build().get()
                    .uri(searchUrl)
                    .headers(h -> h.setBearerAuth(tokenProvider.provideToken()))
                    .retrieve()
                    .bodyToMono(ItunesResponse.class)
                    .map(ItunesResponse::getResults);
        } catch (URISyntaxException e) {
            return Mono.error(e);
        }
    }
}
