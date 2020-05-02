package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResponse;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesSongsData;
import com.dharbar.template.service.musicresources.itunes.devapi.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Service
public class DevApiItunesRequester {

    private final TokenProvider tokenProvider;
    private final URIBuilder searchUriBuilder = new URIBuilder()
            .setScheme("https")
            .setHost("api.music.apple.com")
            .setPath("/v1/catalog/us/search");

    private final WebClient.Builder webClientBuilder;

    public DevApiItunesRequester(TokenProvider tokenProvider,
                                 WebClient.Builder webClientBuilder) {
        this.tokenProvider = tokenProvider;
        this.webClientBuilder = webClientBuilder;
    }

    public Flux<ItunesSongsData> request(List<NameValuePair> params) {
        return requestLimited(params);
    }

    public Mono<ItunesSongsData> requestOne(List<NameValuePair> params) {
        return requestLimited(params).singleOrEmpty();
    }

    private Flux<ItunesSongsData> requestLimited(List<NameValuePair> params) {
        try {
            var searchUrl = searchUriBuilder
//                    .addParameter("limit", String.valueOf(limit))
                    .addParameters(params)
                    .build();
            log.info("sending to Itunes uri {}", searchUrl.toString());

            return webClientBuilder.build().get()
                    .uri(searchUrl)
                    .header("Authorization", "Bearer " + tokenProvider.provideToken())
                    .retrieve()
                    .bodyToMono(ItunesResponse.class)
                    .flatMapMany(this::extractData);
        } catch (URISyntaxException e) {
            return Flux.error(e);
        }
    }

    private Flux<ItunesSongsData> extractData(ItunesResponse itunesResponse) {
        return Flux.fromIterable(itunesResponse.getResults().getSongs().getData());
    }
}
