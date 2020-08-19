package mx.conacyt.crip.mail.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.in.RegisterUserCommand;
import mx.conacyt.crip.mail.application.port.in.RegisterUserUseCase;
import mx.conacyt.crip.mail.security.AuthoritiesConstants;
import mx.conacyt.crip.mail.web.api.UsersApiDelegate;
import mx.conacyt.crip.mail.web.model.User;

@Secured(AuthoritiesConstants.ADMIN)
@Component
public class UserResource implements UsersApiDelegate {

    private final RegisterUserUseCase service;

    public UserResource(RegisterUserUseCase service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Void> registerUser(User user) {
        service.registerUser(new RegisterUserCommand(user.getName()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
