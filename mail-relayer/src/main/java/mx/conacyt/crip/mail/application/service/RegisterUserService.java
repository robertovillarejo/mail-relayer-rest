package mx.conacyt.crip.mail.application.service;

import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.application.port.in.RegisterUserCommand;
import mx.conacyt.crip.mail.application.port.in.RegisterUserUseCase;
import mx.conacyt.crip.mail.application.port.out.CreateUserPort;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.events.UserCreated;
import mx.conacyt.crip.mail.domain.exception.UsernameAlreadyExists;

/**
 * Implementación del caso de usuario de registrar un usuario.
 */
@RequiredArgsConstructor
class RegisterUserService implements RegisterUserUseCase {

    private final CreateUserPort createUserPort;
    private final LoadUserPort loadUserPort;

    @Override
    public User registerUser(RegisterUserCommand command) {
        if (loadUserPort.existsUser(command.getName())) {
            throw new UsernameAlreadyExists();
        }
        User user = createUserPort.createUser(command.getName(), command.getMsgIdSuffix());
        DomainEventBus.EVENT_BUS.post(new UserCreated(user));
        return user;
    }

}
