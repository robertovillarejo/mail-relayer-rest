package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.google.common.eventbus.Subscribe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.application.port.out.SaveEmailPort;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.events.EmailAsyncQueued;
import mx.conacyt.crip.mail.domain.events.EmailAsyncReceived;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * Prueba del servicio {@link SendMailService}.
 */
@TestInstance(Lifecycle.PER_CLASS)
public class SendMailServiceTest {

    private static final int ASYNC_SUCCESS_VERIFICATION_TIMEOUT = 5000;
    private static final int ASYNC_FAIL_VERIFICATION_TIMEOUT = 2000;

    private static final String BODY = "Mensaje";
    private static final String SUBJECT = "Asunto";
    private static final String SENDER = "no-reply@example.com";
    private static final String RECIPIENT = "user@localhost.com";
    private static final String USER_ID = "a1b2c3";
    private static final String MESSAGE_ID_SUFFIX = "example.com";

    private SimpleSmtpServer server;
    private Mailer mailer;
    private SendMailUseCase sendMailService;
    private EmailAcknowledger acknowledger;
    private SaveEmailPort saveEmailPort;
    private LoadUserPort loadUserPort;
    private boolean eventHappened = Boolean.FALSE;

    @BeforeAll
    public void setup() throws IOException {
        DomainEventBus.EVENT_BUS.register(this);
        acknowledger = Mockito.mock(EmailAcknowledger.class);
        saveEmailPort = Mockito.mock(SaveEmailPort.class);
        loadUserPort = Mockito.mock(LoadUserPort.class);
        when(loadUserPort.loadUser(eq(USER_ID))).thenReturn(Optional.of(user()));
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);
        mailer = MailerBuilder.withSMTPServer("127.0.0.1", server.getPort()).buildMailer();
        sendMailService = new SendMailService(mailer, saveEmailPort, loadUserPort, acknowledger);
    }

    @Test
    public void sendMailSync() {
        server.reset();
        // Given
        SendMailCommand cmd = new SendMailCommand(givenEmail(), USER_ID, false);
        // When
        String msgId = sendMailService.sendMail(cmd);
        // Then
        assertEquals(1, server.getReceivedEmails().size());
        verifySentEmail(msgId);
    }

    @Test
    public void sendMailAsyncSuccess() {
        server.reset();
        // Given
        Email email = givenEmail();
        SendMailCommand cmd = new SendMailCommand(email, USER_ID, true);
        // When
        String msgId = sendMailService.sendMail(cmd);
        // Then
        verify(acknowledger, timeout(ASYNC_SUCCESS_VERIFICATION_TIMEOUT)).success(eq(msgId));
        assertEquals(1, server.getReceivedEmails().size());
        assertTrue(eventHappened);
        verifySentEmail(msgId);
    }

    @Test
    public void sendMailAsyncFailsAndNotify() throws IOException {
        // Given
        server.stop();
        Email email = givenEmail();
        SendMailCommand cmd = new SendMailCommand(email, USER_ID, true);
        // When
        String msgId = sendMailService.sendMail(cmd);
        // Then
        verify(acknowledger, timeout(ASYNC_FAIL_VERIFICATION_TIMEOUT)).fail(eq(msgId), any());
        // Teardown
        server = SimpleSmtpServer.start(server.getPort());
    }

    @Test
    public void sendMailFailWithNotExistentUser() {
        // Given
        SendMailCommand cmd = new SendMailCommand(givenEmail(), "esteIdNoExiste", true);
        // Then
        assertThrows(UserNotExists.class, () -> {
            // When
            sendMailService.sendMail(cmd);
        });
    }

    /**
     * Simula comportamiento de una cola de trabajo.
     *
     * Simplemente escucha el evento de que se ha recibido un email y emite uno
     * nuevo anunciando que se ha encolado un email.
     *
     * @param event el evento de email recibido por el puerto de entrada.
     */
    @Subscribe
    public void processAsyncMail(EmailAsyncReceived event) {
        eventHappened = Boolean.TRUE;
        DomainEventBus.EVENT_BUS.post(new EmailAsyncQueued(event.getEmail(), event.getUsername()));
    }

    public static Email givenEmail() {
        return EmailBuilder.startingBlank().from(SENDER).toMultiple(Arrays.asList(RECIPIENT)).withSubject(SUBJECT)
                .withPlainText(BODY).buildEmail();
    }

    private User user() {
        return new User("user", MESSAGE_ID_SUFFIX);
    }

    private void verifySentEmail(String msgId) {
        SmtpMessage msg = server.getReceivedEmails().iterator().next();
        assertEquals(msgId, msg.getHeaderValue("Message-ID"));
        assertEquals(BODY, msg.getBody());
        assertEquals(SUBJECT, msg.getHeaderValue("Subject"));
        assertEquals(SENDER, msg.getHeaderValue("From"));
        assertEquals(RECIPIENT, msg.getHeaderValue("To"));
    }

}
