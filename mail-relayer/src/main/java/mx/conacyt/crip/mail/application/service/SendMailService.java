package mx.conacyt.crip.mail.application.service;

import java.time.Instant;
import java.util.Optional;

import com.google.common.eventbus.Subscribe;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.AsyncResponse;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import lombok.extern.slf4j.Slf4j;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.application.port.out.SaveEmailPort;
import mx.conacyt.crip.mail.domain.User;
import mx.conacyt.crip.mail.domain.events.EmailAsyncQueued;
import mx.conacyt.crip.mail.domain.events.EmailAsyncReceived;
import mx.conacyt.crip.mail.domain.exception.UserNotExists;

/**
 * Implementación del caso de uso de enviar un email.
 */
@Slf4j
class SendMailService implements SendMailUseCase {

    private final Mailer mailer;
    private final SaveEmailPort saveEmailPort;
    private final LoadUserPort loadUserPort;
    private final EmailAcknowledger notifier;

    /**
     * Crea un nuevo servicio.
     *
     * @param mailer        el mailer a usar para enviar los email.
     * @param saveEmailPort el puerto para guardar los email.
     * @param loadUserPort  el puerto para cargar usuarios.
     * @param notifier      el puerto para notificar éxito y fallo de envío de
     *                      email.
     */
    SendMailService(Mailer mailer, SaveEmailPort saveEmailPort, LoadUserPort loadUserPort, EmailAcknowledger notifier) {
        DomainEventBus.EVENT_BUS.register(this);
        this.mailer = mailer;
        this.saveEmailPort = saveEmailPort;
        this.loadUserPort = loadUserPort;
        this.notifier = notifier;
    }

    @Override
    public String sendMail(SendMailCommand command) {
        Optional<User> maybeUser = loadUserPort.loadUser(command.getUsername());
        if (!maybeUser.isPresent()) {
            throw new UserNotExists();
        }
        String messageId = messageId(maybeUser.get());
        Email emailWithId = EmailBuilder.copying(command.getEmail()).fixingMessageId(messageId).buildEmail();
        if (!command.isAsync()) {
            return sendMailSync(emailWithId, command.getUsername());
        }
        return sendMailAsync(emailWithId, command.getUsername());
    }

    private String sendMailSync(Email email, String username) {
        log.debug("Enviando correo de forma síncrona");
        mailer.sendMail(email);
        saveEmailPort.saveEmail(email, username);
        return email.getId();
    }

    private String sendMailAsync(Email email, String username) {
        log.debug("Enviando correo de forma asíncrona");
        DomainEventBus.EVENT_BUS.post(new EmailAsyncReceived(email, username));
        return email.getId();
    }

    /**
     * Procesa un email encolado en la cola de trabajo.
     *
     * @param event el evento de email encolado.
     */
    @Subscribe
    public void processAsyncMail(EmailAsyncQueued event) {
        Email email = event.getEmail();
        String emailId = email.getId();
        String username = event.getUsername();
        AsyncResponse response = mailer.sendMail(email, Boolean.TRUE);
        response.onSuccess(() -> {
            notifier.success(emailId);
            saveEmailPort.saveEmail(email, username);
        });
        response.onException(e -> {
            notifier.fail(emailId, e);
            saveEmailPort.saveEmail(email, username);
        });
    }

    private String messageId(User user) {
        Instant now = Instant.now();
        String id = String.format("%s.%s", now.getEpochSecond(), now.getNano());
        return String.format("<%s@%s>", id, user.getMessageIdSuffix());
    }

}
