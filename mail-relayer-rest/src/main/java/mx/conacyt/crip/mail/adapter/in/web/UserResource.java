package mx.conacyt.crip.mail.adapter.in.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.in.GetSecretKeyQuery;
import mx.conacyt.crip.mail.application.port.in.RegisterUserCommand;
import mx.conacyt.crip.mail.application.port.in.RegisterUserUseCase;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.security.AuthoritiesConstants;
import mx.conacyt.crip.mail.web.api.UsersApiDelegate;
import mx.conacyt.crip.mail.web.model.User;
import mx.conacyt.crip.mail.web.model.UserRegistration;

@Secured(AuthoritiesConstants.ADMIN)
@Component
public class UserResource implements UsersApiDelegate {

    private final RegisterUserUseCase service;
    private final GetSecretKeyQuery getSecretKeyQuery;

    public UserResource(RegisterUserUseCase service, GetSecretKeyQuery getSecretKeyQuery) {
        this.service = service;
        this.getSecretKeyQuery = getSecretKeyQuery;
    }

    @Override
    public ResponseEntity<Void> registerUser(UserRegistration info) {
        service.registerUser(new RegisterUserCommand(info.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<User> getUserByName(String name) {
        Optional<SecretKey> maybeSecretKey = getSecretKeyQuery.getSecretKey(name);
        if (maybeSecretKey.isPresent()) {
            User user = new User().name(name).apiKey(maybeSecretKey.get().getContent());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

}
