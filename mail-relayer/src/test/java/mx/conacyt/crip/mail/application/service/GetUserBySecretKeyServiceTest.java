package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import mx.conacyt.crip.mail.application.port.in.GetUserBySecretKeyQuery;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * Prueba del servicio {@link GetUserBySecretKeyService}.
 */
public class GetUserBySecretKeyServiceTest {

    private static final String SECRET_KEY_CONTENT = "abc123abc123abc123abc123";
    private static final String USER_ID = "a1b2c3";
    private static final String USER_NAME = "username";

    private GetUserBySecretKeyQuery getUserBySecretKeyQuery;
    private LoadUserPort loadUserPort;

    @BeforeEach
    public void setup() {
        loadUserPort = Mockito.mock(LoadUserPort.class);
        when(loadUserPort.loadUserBySecretKey(eq(SecretKey.of(SECRET_KEY_CONTENT)))).thenReturn(Optional.of(user()));
        getUserBySecretKeyQuery = new GetUserBySecretKeyService(loadUserPort);
    }

    @Test
    public void getUserBySecretKey() {
        // Given
        SecretKey sk = SecretKey.of(SECRET_KEY_CONTENT);
        // When
        Optional<User> maybeUser = getUserBySecretKeyQuery.getUserBySecretKey(sk);
        // Then
        assertEquals(user(), maybeUser.get());
    }

    private User user() {
        return new User(USER_NAME);
    }

}
