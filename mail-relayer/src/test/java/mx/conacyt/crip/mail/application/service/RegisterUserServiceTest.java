package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.eventbus.Subscribe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mx.conacyt.crip.mail.application.port.in.RegisterUserCommand;
import mx.conacyt.crip.mail.application.port.in.RegisterUserUseCase;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.events.UserCreated;
import mx.conacyt.crip.mail.domain.exception.UsernameAlreadyExists;

/**
 * Prueba del servicio {@link RegisterUserService}.
 */
public class RegisterUserServiceTest {

    private static final String USERNAME = "bob";
    private static final String MSG_ID_SUFFIX = "bob.com";

    private RegisterUserUseCase service;
    private UserPersistenceAdapterInMemory userPersistence;
    private boolean eventHappened = Boolean.FALSE;

    @BeforeEach
    public void setup() {
        userPersistence = new UserPersistenceAdapterInMemory();
        service = new RegisterUserService(userPersistence, userPersistence);
    }

    @Test
    public void registerUserSuccess() throws UsernameAlreadyExists {
        DomainEventBus.EVENT_BUS.register(this);
        // Given
        RegisterUserCommand cmd = new RegisterUserCommand(USERNAME, MSG_ID_SUFFIX);
        // When
        User user = service.registerUser(cmd);
        // Then
        assertEquals(USERNAME, user.getName());
        assertTrue(userPersistence.existsUser(USERNAME));
        assertTrue(eventHappened);
    }

    @Test
    public void registerUserWithExistingName() throws UsernameAlreadyExists {
        // Given
        RegisterUserCommand cmd = givenCommand();
        service.registerUser(cmd);
        DomainEventBus.EVENT_BUS.register(this);
        // Then
        assertThrows(UsernameAlreadyExists.class, () -> {
            // When
            service.registerUser(cmd);
        });
        assertFalse(eventHappened);
    }

    @Subscribe
    public void onUserCreated(UserCreated event) {
        eventHappened = Boolean.TRUE;
    }

    private RegisterUserCommand givenCommand() {
        return new RegisterUserCommand(USERNAME, MSG_ID_SUFFIX);
    }

}
