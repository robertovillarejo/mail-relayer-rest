package mx.conacyt.crip.mail.application.port.in;

import java.util.Optional;

import lombok.NonNull;
import mx.conacyt.crip.mail.domain.Mail;

/**
 * Caso de uso para consultar un {@code Mail} por su Message-ID.
 */
public interface GetMailQuery {

    /**
     * Consulta un {@code Email} por su Message-ID.
     *
     * @param msgId    el Message-ID del email.
     * @param username el nombre del usuario quien realiza la consulta.
     * @return el email dentro o vacío si no se encontró.
     */
    Optional<Mail> getMailById(@NonNull String msgId, @NonNull String username);

}
