package mx.conacyt.crip.mail.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import mx.conacyt.crip.mail.application.port.in.SendMailCommand;

@TestInstance(Lifecycle.PER_CLASS)
public class SendMailServiceTest {

    private static final String BODY = "Mensaje";
    private static final String SUBJECT = "Asunto";
    private static final String SENDER = "no-reply@example.com";
    private static final String RECIPIENT = "user@localhost.com";

    private SimpleSmtpServer server;
    private Mailer mailer;
    private SendMailService sendMailService;

    @BeforeAll
    public void setup() throws IOException {
        server = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT);
        mailer = MailerBuilder.withSMTPServer("127.0.0.1", server.getPort()).buildMailer();
        sendMailService = new SendMailService(mailer);
    }

    @Test
    void sendMail() {
        // Given
        server.reset();
        // When
        sendMailService.sendMail(new SendMailCommand(EmailBuilder.startingBlank().from(SENDER)
                .toMultiple(Arrays.asList(RECIPIENT)).withSubject(SUBJECT).withPlainText(BODY).buildEmail()));
        // Then
        assertEquals(1, server.getReceivedEmails().size());
        SmtpMessage msg = server.getReceivedEmails().iterator().next();
        assertEquals(BODY, msg.getBody());
        assertEquals(SUBJECT, msg.getHeaderValue("Subject"));
        assertEquals(SENDER, msg.getHeaderValue("From"));
        assertEquals(RECIPIENT, msg.getHeaderValue("To"));
    }

}
