package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import mx.conacyt.crip.mail.application.port.in.GenerateSecretKeyUseCase;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.application.port.out.SaveSecretKeyPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.events.UserCreated;

/**
 * Prueba del servicio {@link GenerateSecretKeyService}.
 */
public class GenerateSecretKeyServiceTest {

    private static final String USER_NAME = "username";

    private GenerateSecretKeyUseCase service;
    private SaveSecretKeyPort saveSecretKeyPort;
    private LoadUserPort loadUserPort;

    @BeforeEach
    public void setup() {
        saveSecretKeyPort = Mockito.mock(SaveSecretKeyPort.class);
        loadUserPort = Mockito.mock(LoadUserPort.class);
        when(loadUserPort.loadUser(anyString())).thenReturn(Optional.of(givenUser()));
        service = new GenerateSecretKeyService(saveSecretKeyPort, loadUserPort);
    }

    @Test
    public void generateSecretKey() {
        // Given
        User user = givenUser();
        // When
        SecretKey secretKey = service.generateSecretKey(user.getName());
        // Then
        verify(saveSecretKeyPort).saveSecretKey(any(), eq(user));
        assertNotNull(secretKey.getContent());
    }

    @Test
    public void generateSecretkeyOnUserCreatedEvent() {
        // Given
        User user = givenUser();
        UserCreated event = new UserCreated(user);
        // When
        DomainEventBus.EVENT_BUS.post(event);
        // Then
        verify(saveSecretKeyPort).saveSecretKey(any(), eq(user));
    }

    private User givenUser() {
        return new User(USER_NAME);
    }

}
