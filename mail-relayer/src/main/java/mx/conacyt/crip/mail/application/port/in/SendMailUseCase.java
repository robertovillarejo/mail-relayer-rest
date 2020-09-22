package mx.conacyt.crip.mail.application.port.in;

import java.util.Objects;

import lombok.Getter;
import mx.conacyt.crip.mail.domain.Mail;

/**
 * El caso de uso de enviar {@link Mail}.
 */
public interface SendMailUseCase {

    /**
     * Envía un email.
     *
     * @param command el comando con la información necesaria.
     * @return el id del email enviado si fue síncrono o a enviar si es asíncrono.
     */
    String sendMail(SendMailCommand command);

    /**
     * El comando de enviar {@link Mail}.
     */
    @Getter
    class SendMailCommand {

        private final Mail mail;
        private final boolean async;
        private final String username;

        /**
         * Crea un nuevo comando de enviar email.
         *
         * @param mail     el email a enviar.
         * @param username el nombre del usuario quien envía.
         * @param async    false si síncrono | true si es asíncrono
         */
        public SendMailCommand(final Mail mail, String username, final boolean async) {
            Objects.requireNonNull(mail.getFromRecipient());
            Objects.requireNonNull(username);
            Objects.requireNonNull(mail.getRecipients());
            this.mail = mail;
            this.username = username;
            this.async = async;
        }

    }

}
