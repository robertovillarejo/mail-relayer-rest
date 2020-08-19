package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import mx.conacyt.crip.mail.application.port.in.GetSecretKeyQuery;
import mx.conacyt.crip.mail.application.port.out.LoadSecretKeyPort;
import mx.conacyt.crip.mail.domain.SecretKey;

/**
 * Prueba del servicio {@link GetSecretKeyService}.
 */
public class GetSecretKeyServiceTest {

    private static final String USER_ID = "a1b2c3";
    private static final String SECRET_KEY_CONTENT = "abc123abc123abc123abc123";

    private GetSecretKeyQuery service;
    private LoadSecretKeyPort loadSecretKeyPort;

    @BeforeEach
    public void setup() {
        loadSecretKeyPort = Mockito.mock(LoadSecretKeyPort.class);
        when(loadSecretKeyPort.loadSecretKey(eq(USER_ID))).thenReturn(Optional.of(SecretKey.of(SECRET_KEY_CONTENT)));
        service = new GetSecretKeyService(loadSecretKeyPort);
    }

    @Test
    public void getSecretKey() {
        // Given
        // When
        Optional<SecretKey> maybeSk = service.getSecretKey(USER_ID);
        // Then
        assertEquals(SecretKey.of(SECRET_KEY_CONTENT), maybeSk.get());
    }

}
