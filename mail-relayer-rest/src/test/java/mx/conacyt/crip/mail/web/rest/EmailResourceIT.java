package mx.conacyt.crip.mail.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.simplejavamail.api.mailer.Mailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import mx.conacyt.crip.mail.MailrelayerApp;
import mx.conacyt.crip.mail.adapter.in.web.EmailResource;
import mx.conacyt.crip.mail.config.TestSecurityConfiguration;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.UserRepository;
import mx.conacyt.crip.mail.security.AuthoritiesConstants;
import mx.conacyt.crip.mail.web.model.EmailDto;

/**
 * Integration tests for the {@link EmailResource} REST controller.
 */
@WithMockUser(username = "user", authorities = AuthoritiesConstants.USER)
@AutoConfigureMockMvc
@SpringBootTest(classes = { MailrelayerApp.class, TestSecurityConfiguration.class })
public class EmailResourceIT {

    private static final String BODY = "Mensaje";
    private static final String SUBJECT = "Asunto";
    private static final String SENDER = "no-reply@example.com";
    private static final String RECIPIENT = "user@localhost.com";

    @Autowired
    private MockMvc restEmailMockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private Mailer mailer;

    @BeforeEach
    public void setupEach() throws IOException {
        UserMongoEntity user = new UserMongoEntity();
        user.setName("user");
        user.setMessageIdSuffix("example.com");
        when(userRepository.findByName("user")).thenReturn(Optional.of(user));
    }

    @Test
    public void sendMailSync() throws Exception {
        // Given
        EmailDto email = givenEmail();
        // When
        MvcResult mvcResult = restEmailMockMvc
                .perform(post("/api/emails").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonBytes(email)))
                // Then
                .andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn();
        String msgId = mvcResult.getResponse().getHeader("Location");

        ArgumentCaptor<org.simplejavamail.api.email.Email> argumentCaptor = ArgumentCaptor
                .forClass(org.simplejavamail.api.email.Email.class);
        verify(mailer).sendMail(argumentCaptor.capture());
        org.simplejavamail.api.email.Email capturedArgument = argumentCaptor.getValue();
        assertEquals("<" + msgId.replace("mid:", "") + ">", capturedArgument.getId());
    }

    @Test
    public void sendMailAsyncSuccess() throws IOException, Exception {
        // Given
        EmailDto email = givenEmail();
        // When
        restEmailMockMvc
                .perform(post("/api/emails?async=true").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonBytes(email)))
                // Then
                .andExpect(status().isAccepted()).andExpect(header().exists("Location")).andReturn();
    }

    @Test
    public void sendMailAsyncFailsAndNotify() throws Exception {
        // Given
        EmailDto email = givenEmail();
        // When
        restEmailMockMvc
                .perform(post("/api/emails?async=true").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonBytes(email)))
                // Then
                .andExpect(status().isAccepted()).andExpect(header().exists("Location"));
    }

    private EmailDto givenEmail() {
        return new EmailDto().sender(SENDER).to(Arrays.asList(RECIPIENT)).plainBody(BODY).subject(SUBJECT);
    }

}
