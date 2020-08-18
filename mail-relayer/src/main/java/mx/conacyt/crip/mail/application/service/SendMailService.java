package mx.conacyt.crip.mail.application.service;

import java.time.Instant;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.AsyncResponse;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;

@Slf4j
@AllArgsConstructor
class SendMailService implements SendMailUseCase {

    private static final String MSG_ID_TEMPLATE = "<%s@%s>";

    private final Mailer mailer;
    private EmailAcknowledger notifier;

    public SendMailService(Mailer mailer) {
        this.mailer = mailer;
    }

    @Override
    public String sendMail(SendMailCommand command) {
        if (!command.isAsync()) {
            return sendMailSync(command);
        }
        return sendMailAsync(command);
    }

    public String sendMailSync(SendMailCommand command) {
        log.debug("Enviando correo de forma síncrona");
        String msgId = messageId();
        final Email email = EmailBuilder.copying(command.getEmail()).fixingMessageId(msgId).buildEmail();
        mailer.sendMail(email);
        return msgId;
    }

    public String sendMailAsync(SendMailCommand command) {
        log.debug("Enviando correo de forma asíncrona");
        String msgId = messageId();
        Email email = EmailBuilder.copying(command.getEmail()).fixingMessageId(msgId).buildEmail();
        AsyncResponse response = mailer.sendMail(email, Boolean.TRUE);
        if (notifier != null) {
            response.onSuccess(() -> notifier.success(msgId));
            response.onException(e -> notifier.fail(msgId, e));
        }
        return msgId;
    }

    private String messageId() {
        Instant now = Instant.now();
        String id = String.format("%s.%s", now.getEpochSecond(), now.getNano());
        return String.format(MSG_ID_TEMPLATE, id, "localhost.com");
    }

}
