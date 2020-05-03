package com.dharbar.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SuppressWarnings("checkstyle:hideutilityclassconstructor")
//@EnableSwagger2WebFlux
@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
