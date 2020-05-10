package com.dharbar.template.service.melodypref.service.it;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles("mongo")
@ContextConfiguration(initializers = MongoIntegrationTest.MongoDbInitializer.class)
@SpringBootTest
@Slf4j
public class MongoIntegrationTest {

    private static MongoDbContainer mongoDbContainer;

    @BeforeAll
    public static void init() {
        mongoDbContainer = new MongoDbContainer();
        mongoDbContainer.start();
    }

    public static class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            log.info("Overriding Spring Properties for mongodb");

            TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongoDbContainer.getContainerIpAddress(),
                    "spring.data.mongodb.port=" + mongoDbContainer.getPort()
            ).applyTo(configurableApplicationContext);
        }
    }
}
