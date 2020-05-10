package com.dharbar.template.service.musicresources.itunes.devapi.token.generator;

import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
class SimpleTokenGeneratorTest {

    private TokenGenerator target;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        target = new TokenGenerator();
    }

    @Test
    void getToken_returnEmpty_invalidParameters() {
        // when
        Optional<String> result = target.getToken("test", "test", "test");

        // then
        assertThat(result).isEmpty();
    }
}
