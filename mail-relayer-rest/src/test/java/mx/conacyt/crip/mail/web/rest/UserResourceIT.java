package mx.conacyt.crip.mail.web.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import mx.conacyt.crip.mail.MailrelayerApp;
import mx.conacyt.crip.mail.adapter.in.web.UserResource;
import mx.conacyt.crip.mail.config.TestSecurityConfiguration;
import mx.conacyt.crip.mail.domain.SecretKeyMongoEntity;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.SecretKeyRepository;
import mx.conacyt.crip.mail.repository.UserRepository;
import mx.conacyt.crip.mail.security.AuthoritiesConstants;
import mx.conacyt.crip.mail.web.model.UserRegistration;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@AutoConfigureMockMvc
@SpringBootTest(classes = { MailrelayerApp.class, TestSecurityConfiguration.class })
public class UserResourceIT {

    public static final String USERNAME = "awesomeapp";
    public static final String SECRET_KEY_CONTENT = "awesomeapp";
    private static final String MESSAGE_ID_SUFFIX = "conacyt.mx";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecretKeyRepository secretKeyRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        secretKeyRepository.deleteAll();
    }

    @Test
    public void registerUser() throws IOException, Exception {
        // Given
        UserRegistration user = givenNewUser();
        // When
        postUser(user)
                // Then
                .andExpect(status().isCreated());
        assertTrue(userRepository.existsByName(USERNAME));
        assertEquals(1, secretKeyRepository.count());
    }

    @Test
    public void registerUserWithExistingName() throws IOException, Exception {
        // Given
        UserRegistration user = givenNewUser();
        userRepository.save(new UserMongoEntity().name(USERNAME));
        // When
        postUser(user)
                // Then
                .andExpect(status().isBadRequest());
        assertEquals(0, secretKeyRepository.count());
    }

    @WithMockUser(username = "user", authorities = AuthoritiesConstants.USER)
    @Test
    public void registerUserFailWithForbiddenUser() throws IOException, Exception {
        // Given
        UserRegistration user = givenNewUser();
        // When
        postUser(user)
                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserByName() throws Exception {
        // Given
        UserMongoEntity user = new UserMongoEntity().name(USERNAME);
        userRepository.save(user);
        secretKeyRepository.save(new SecretKeyMongoEntity().user(user).content(SECRET_KEY_CONTENT));
        // When
        restUserMockMvc.perform(get("/api/users/{name}", USERNAME).contentType(MediaType.APPLICATION_JSON_VALUE))
                // Then
                .andExpect(status().isOk()).andExpect(jsonPath("$.apiKey").value(SECRET_KEY_CONTENT))
                .andExpect(jsonPath("$.name").value(USERNAME));
    }

    public ResultActions postUser(UserRegistration newUser) throws IOException, Exception {
        return restUserMockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(newUser)));
    }

    public UserRegistration givenNewUser() {
        return new UserRegistration().name(USERNAME).messageIdSuffix(MESSAGE_ID_SUFFIX);
    }

}
