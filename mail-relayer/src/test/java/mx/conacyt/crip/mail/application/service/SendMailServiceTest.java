package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;

@TestInstance(Lifecycle.PER_CLASS)
public class SendMailServiceTest {

    private static final String BODY = "Mensaje";
    private static final String SUBJECT = "Asunto";
    private static final String SENDER = "no-reply@example.com";
    private static final String RECIPIENT = "user@localhost.com";

    private SimpleSmtpServer server;
    private Mailer mailer;
    private SendMailUseCase sendMailService;
    private EmailAcknowledger acknowledger;

    @BeforeAll
    public void setup() throws IOException {
        acknowledger = Mockito.mock(EmailAcknowledger.class);
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);
        mailer = MailerBuilder.withSMTPServer("127.0.0.1", server.getPort()).buildMailer();
        sendMailService = new SendMailService(mailer, acknowledger);
    }

    @Test
    public void sendMailSync() {
        // Given
        server.reset();
        // When
        sendMailService.sendMail(new SendMailCommand(email(), false));
        // Then
        assertEquals(1, server.getReceivedEmails().size());
        verifySentEmail();
    }

    @Test
    public void sendMailAsyncSuccess() {
        // Given
        server.reset();
        // When
        String msgId = sendMailService.sendMail(new SendMailCommand(email(), true));
        // Then
        verify(acknowledger, timeout(5000)).success(msgId);
        assertEquals(1, server.getReceivedEmails().size());
        verifySentEmail();
    }

    @Test
    public void sendMailAsyncFailsAndNotify() throws IOException {
        // Given
        server.stop();
        // When
        String msgId = sendMailService.sendMail(new SendMailCommand(email(), true));
        // Then
        verify(acknowledger, timeout(2000)).fail(eq(msgId), any());
        // Teardown
        server = SimpleSmtpServer.start(server.getPort());
    }

    private Email email() {
        return EmailBuilder.startingBlank().from(SENDER).toMultiple(Arrays.asList(RECIPIENT)).withSubject(SUBJECT)
                .withPlainText(BODY).buildEmail();
    }

    private void verifySentEmail() {
        SmtpMessage msg = server.getReceivedEmails().iterator().next();
        assertEquals(BODY, msg.getBody());
        assertEquals(SUBJECT, msg.getHeaderValue("Subject"));
        assertEquals(SENDER, msg.getHeaderValue("From"));
        assertEquals(RECIPIENT, msg.getHeaderValue("To"));
    }

}
