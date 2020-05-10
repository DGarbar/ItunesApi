package com.dharbar.template.service.musicresources.itunes.devapi;

import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResponse;
import com.dharbar.template.service.musicresources.itunes.devapi.dto.devapi.ItunesResult;
import com.dharbar.template.service.musicresources.itunes.devapi.token.TokenProvider;
import com.dharbar.template.service.musicresources.itunes.devapi.token.exception.NoTokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    @Value("${itunes.dev.scheme}")
    private String itunesScheme;
    @Value("${itunes.dev.host}")
    private String itunesHost;
    @Value("${itunes.dev.path}")
    private String itunesPathSearch;

    public DevApiItunesRequestSearcher(TokenProvider tokenProvider,
                                       WebClient.Builder webClientBuilder) {
        this.tokenProvider = tokenProvider;
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<ItunesResult> request(List<NameValuePair> params) {
        return requestLimited(params);
    }

//    public Mono<ItunesResult> requestOne(List<NameValuePair> params) {
//        return requestLimited(params);
//    }

    // TODO TEST artist - song
    // TODO LIMIT
    private Mono<ItunesResult> requestLimited(List<NameValuePair> params) {
        try {
            var searchUrl = new URIBuilder()
                    .setScheme(itunesScheme)
                    .setHost(itunesHost)
                    .setPath(itunesPathSearch)
//                    .addParameter("limit", String.valueOf(limit))
                    .addParameters(params)
                    .build();
            log.info("Sending to Itunes uri {}", searchUrl.toString());
            String token = tokenProvider.provideToken();

            return webClientBuilder.build().get()
                    .uri(searchUrl)
                    .headers(h -> h.setBearerAuth(token))
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> Mono.error(
                            new IllegalArgumentException("Server responded with badRequest")))
                    .bodyToMono(ItunesResponse.class)
                    .map(ItunesResponse::getResults);
        } catch (URISyntaxException e) {
            return Mono.error(() -> new InternalError(e));
        } catch (NoTokenException e) {
            return Mono.error(e);
        }
    }
}
