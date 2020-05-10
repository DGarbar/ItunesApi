package com.dharbar.template.service.musicresources.itunes.devapi.token;

import com.dharbar.template.service.musicresources.itunes.devapi.token.exception.NoTokenException;
import com.dharbar.template.service.musicresources.itunes.devapi.token.generator.TokenGenerator;
import com.dharbar.template.utils.ShouldWhenUnderscoreNameGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(ShouldWhenUnderscoreNameGenerator.class)
@ExtendWith(MockitoExtension.class)
class TokenProviderTest {
    private static final String TOKEN = "token";

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private TokenProvider target;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        given(tokenGenerator.getToken()).willReturn(Optional.of(TOKEN));
    }

    @Test
    void provideToken_returnToken_generatorReturnToken() throws NoTokenException {
        // when
        var result = target.provideToken();

        // then
        assertThat(result).isEqualTo(TOKEN);

        verify(tokenGenerator).getToken();
    }

    @Test
    void provideToken_throwException_generatorReturnEmpty() {
        // given
        given(tokenGenerator.getToken()).willReturn(Optional.empty());

        // when and then
        assertThatThrownBy(() -> target.provideToken())
                .isInstanceOf(NoTokenException.class);

        verify(tokenGenerator).getToken();
    }

    @Test
    void provideToken_getFromCache_secondAttemptGetToken() throws NoTokenException {
        // when
        target.provideToken();
        target.provideToken();
        target.provideToken();
        var result = target.provideToken();

        // then
        assertThat(result).isEqualTo(TOKEN);

        // then
        verify(tokenGenerator).getToken();
    }
}
