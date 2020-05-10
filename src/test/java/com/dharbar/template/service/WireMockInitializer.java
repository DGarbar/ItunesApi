package com.dharbar.template.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final int WIREMOCK_PORT = 8090;

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().port(WIREMOCK_PORT));
        wireMockServer.start();

        configurableApplicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);

        configurableApplicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });

        TestPropertyValues
                .of("itunes.dev.host=localhost:" + WIREMOCK_PORT)
                .applyTo(configurableApplicationContext);
    }
}
