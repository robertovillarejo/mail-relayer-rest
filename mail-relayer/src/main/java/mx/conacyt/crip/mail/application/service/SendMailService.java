package mx.conacyt.crip.mail.application.service;

import java.time.Instant;
import java.util.Optional;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.AsyncResponse;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.application.port.out.SaveEmailPort;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * Implementación del caso de uso de enviar un email.
 */
@Slf4j
@RequiredArgsConstructor
class SendMailService implements SendMailUseCase {

    private static final String MSG_ID_TEMPLATE = "<%s@%s>";

    private final Mailer mailer;
    private final SaveEmailPort saveEmailPort;
    private final LoadUserPort loadUserPort;
    private final EmailAcknowledger notifier;

    @Override
    public String sendMail(SendMailCommand command) {
        Optional<User> maybeUser = loadUserPort.loadUser(command.getUsername());
        if (!maybeUser.isPresent()) {
            throw new UserNotExists();
        }
        if (!command.isAsync()) {
            return sendMailSync(command);
        }
        return sendMailAsync(command);
    }

    private String sendMailSync(SendMailCommand command) {
        log.debug("Enviando correo de forma síncrona");
        String msgId = messageId();
        final Email email = EmailBuilder.copying(command.getEmail()).fixingMessageId(msgId).buildEmail();
        mailer.sendMail(email);
        saveEmailPort.saveEmail(email, command.getUsername());
        return msgId;
    }

    private String sendMailAsync(SendMailCommand command) {
        log.debug("Enviando correo de forma asíncrona");
        String msgId = messageId();
        Email email = EmailBuilder.copying(command.getEmail()).fixingMessageId(msgId).buildEmail();
        AsyncResponse response = mailer.sendMail(email, Boolean.TRUE);
        response.onSuccess(() -> {
            notifier.success(msgId);
            saveEmailPort.saveEmail(email, command.getUsername());
        });
        response.onException(e -> {
            notifier.fail(msgId, e);
            saveEmailPort.saveEmail(email, command.getUsername());
        });
        return msgId;
    }

    private String messageId() {
        Instant now = Instant.now();
        String id = String.format("%s.%s", now.getEpochSecond(), now.getNano());
        return String.format(MSG_ID_TEMPLATE, id, "localhost.com");
    }

}
