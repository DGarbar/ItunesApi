package com.dharbar.template.service;

import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/test-config.properties")
@ContextConfiguration(initializers = {WireMockInitializer.class})
public class IntegrationTest {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected WireMockServer wireMockServer;

//    @BeforeEach
//    void configureSystemUnderTest() {
//        WIRE_MOCK = new WireMockServer(8099, ITUNES_WIREMOCK_PORT);
//    }

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    @SneakyThrows
    public String jsonFrom(String path) {
        return mapper.writeValueAsString(mapper.readTree(IntegrationTest.class.getResource(path)));
    }
}
