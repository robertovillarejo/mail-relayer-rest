package mx.conacyt.crip.mail.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import mx.conacyt.crip.mail.MailrelayerApp;
import mx.conacyt.crip.mail.adapter.in.web.EmailResource;
import mx.conacyt.crip.mail.config.ApplicationProperties;
import mx.conacyt.crip.mail.config.TestSecurityConfiguration;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.UserRepository;
import mx.conacyt.crip.mail.security.AuthoritiesConstants;
import mx.conacyt.crip.mail.web.model.Email;

/**
 * Integration tests for the {@link EmailResource} REST controller.
 */
@TestInstance(Lifecycle.PER_CLASS)
@WithMockUser(username = "user", authorities = AuthoritiesConstants.USER)
@AutoConfigureMockMvc
@SpringBootTest(classes = { MailrelayerApp.class, TestSecurityConfiguration.class })
public class EmailResourceIT {

    private static final String BODY = "Mensaje";
    private static final String SUBJECT = "Asunto";
    private static final String SENDER = "no-reply@example.com";
    private static final String RECIPIENT = "user@localhost.com";

    private SimpleSmtpServer server;
    @Autowired
    private ApplicationProperties props;
    @Autowired
    private MockMvc restEmailMockMvc;
    @MockBean
    private UserRepository userRepository;

    @BeforeAll
    public void setup() throws IOException {
        server = SimpleSmtpServer.start(props.getRelayPort());
    }

    @BeforeEach
    public void setupEach() {
        UserMongoEntity user = new UserMongoEntity();
        user.setName("user");
        when(userRepository.findByName("user")).thenReturn(Optional.of(user));
    }

    @Test
    public void sendMailSync() throws Exception {
        // Given
        server.reset();
        // When
        restEmailMockMvc
                .perform(post("/api/emails").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonBytes(email())))
                // Then
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
        verifySentEmail();
    }

    @Test
    public void sendMailAsyncSuccess() throws IOException, Exception {
        // Given
        server.reset();
        // When
        restEmailMockMvc
                .perform(post("/api/emails?async=true").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonBytes(email())))
                // Then
                .andExpect(status().isAccepted()).andExpect(header().exists("Location"));
        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(emailWasReceived(true));
        verifySentEmail();
    }

    @Test
    public void sendMailAsyncFailsAndNotify() throws Exception {
        // Given
        server.stop();
        // When
        restEmailMockMvc
                .perform(post("/api/emails?async=true").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonBytes(email())))
                // Then
                .andExpect(status().isAccepted()).andExpect(header().exists("Location"));
        Awaitility.await().between(1L, TimeUnit.MILLISECONDS, 1L, TimeUnit.SECONDS).until(emailWasReceived(false));
        // Teardown
        server = SimpleSmtpServer.start(server.getPort());
    }

    private Email email() {
        return new Email().from(SENDER).to(Arrays.asList(RECIPIENT)).plainBody(BODY).subject(SUBJECT);
    }

    private void verifySentEmail() {
        SmtpMessage msg = server.getReceivedEmails().iterator().next();
        assertEquals(BODY, msg.getBody());
        assertEquals(SUBJECT, msg.getHeaderValue("Subject"));
        assertEquals(SENDER, msg.getHeaderValue("From"));
        assertEquals(RECIPIENT, msg.getHeaderValue("To"));
    }

    private Callable<Boolean> emailWasReceived(Boolean received) {
        return () -> !received.equals(server.getReceivedEmails().isEmpty());
    }

}
