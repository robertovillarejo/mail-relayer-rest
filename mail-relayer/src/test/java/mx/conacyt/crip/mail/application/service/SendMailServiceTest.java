package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private SimpleSmtpServer server;
    private Mailer mailer;
    private SendMailUseCase sendMailService;
    private EmailAcknowledger acknowledger;
    private SaveEmailPort saveEmailPort;
    private LoadUserPort loadUserPort;

    @BeforeAll
    public void setup() throws IOException {
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
        // Given
        server.reset();
        // When
        sendMailService.sendMail(new SendMailCommand(givenEmail(), USER_ID, false));
        // Then
        assertEquals(1, server.getReceivedEmails().size());
        verifySentEmail();
    }

    @Test
    public void sendMailAsyncSuccess() {
        // Given
        server.reset();
        SendMailCommand cmd = new SendMailCommand(givenEmail(), USER_ID, true);
        // When
        String msgId = sendMailService.sendMail(cmd);
        // Then
        verify(acknowledger, timeout(ASYNC_SUCCESS_VERIFICATION_TIMEOUT)).success(msgId);
        assertEquals(1, server.getReceivedEmails().size());
        verifySentEmail();
    }

    @Test
    public void sendMailAsyncFailsAndNotify() throws IOException {
        // Given
        server.stop();
        SendMailCommand cmd = new SendMailCommand(givenEmail(), USER_ID, true);
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

    private Email givenEmail() {
        return EmailBuilder.startingBlank().from(SENDER).toMultiple(Arrays.asList(RECIPIENT)).withSubject(SUBJECT)
                .withPlainText(BODY).buildEmail();
    }

    private User user() {
        return new User("awesomeapp");
    }

    private void verifySentEmail() {
        SmtpMessage msg = server.getReceivedEmails().iterator().next();
        assertEquals(BODY, msg.getBody());
        assertEquals(SUBJECT, msg.getHeaderValue("Subject"));
        assertEquals(SENDER, msg.getHeaderValue("From"));
        assertEquals(RECIPIENT, msg.getHeaderValue("To"));
    }

}
