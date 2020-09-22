package mx.conacyt.crip.mail.application.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.application.port.in.GetMailQuery;
import mx.conacyt.crip.mail.application.port.out.LoadEmailPort;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.Mail;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * Implementación del caso de uso {@code GetMailQuery}.
 */
@RequiredArgsConstructor
class GetMailService implements GetMailQuery {

    private final LoadEmailPort loadEmailPort;
    private final LoadUserPort loadUserPort;

    @Override
    public Optional<Mail> getMailById(String msgId, String username) {
        if (!loadUserPort.existsUser(username)) {
            throw new UserNotExists();
        }
        return loadEmailPort.loadEmail(msgId, username);
    }

}
