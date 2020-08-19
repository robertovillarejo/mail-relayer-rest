package mx.conacyt.crip.mail.application.port.in;

import org.simplejavamail.api.email.Email;

/**
 * El caso de uso de enviar {@link Email}.
 */
public interface SendMailUseCase {

    /**
     * Envía un email.
     *
     * @param command el comando con la información necesaria.
     * @return el id del email enviado si fue síncrono o a enviar si es asíncrono.
     */
    String sendMail(SendMailCommand command);

}
