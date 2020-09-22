package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.simplejavamail.email.EmailBuilder;

import mx.conacyt.crip.mail.application.port.in.GetMailQuery;
import mx.conacyt.crip.mail.application.port.out.LoadEmailPort;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.Mail;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * Prueba del servicio {@link GetMailService}.
 */
public class GetEmailQueryServiceTest {

    private static final String MSG_ID = "12345678.12345678@example.com";
    private static final String USERNAME = "user";

    private LoadEmailPort loadEmailPort;
    private LoadUserPort loadUserPort;
    private GetMailQuery service;

    @BeforeEach
    public void setup() {
        loadEmailPort = Mockito.mock(LoadEmailPort.class);
        loadUserPort = Mockito.mock(LoadUserPort.class);
        when(loadUserPort.existsUser(eq(USERNAME))).thenReturn(Boolean.TRUE);
        when(loadEmailPort.loadEmail(eq(MSG_ID), eq(USERNAME)))
                .thenReturn(Optional.of(new Mail(EmailBuilder.startingBlank().fixingMessageId(MSG_ID))));
        service = new GetMailService(loadEmailPort, loadUserPort);
    }

    @Test
    public void getEmail() {
        // Given MSG_ID
        // When
        Optional<Mail> maybeEmail = service.getMailById(MSG_ID, USERNAME);
        // Then
        assertEquals(MSG_ID, maybeEmail.get().getId());
    }

    @Test
    public void getEmailWithNotExistentUser() {
        // Given
        final String username = "anonymous";
        // Then
        assertThrows(UserNotExists.class, () -> {
            // When
            service.getMailById(MSG_ID, username);
        });
    }

    @Test
    public void getEmailByNotExistentMessageId() {
        // Given
        final String msgId = "00000000.00000000@example.com";
        // When
        Optional<Mail> maybeMail = service.getMailById(msgId, USERNAME);
        // Then
        assertTrue(maybeMail.isEmpty());
    }

}
