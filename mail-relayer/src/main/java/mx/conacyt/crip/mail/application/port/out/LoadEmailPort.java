package mx.conacyt.crip.mail.application.port.out;

import java.util.Optional;

import javax.annotation.Nonnull;

import mx.conacyt.crip.mail.domain.Mail;

/**
 * Puerto para cargar un {@code Email} por su Message-ID.
 */
public interface LoadEmailPort {

    /**
     * Carga un {@code Email} por su Message-ID.
     *
     * @param messageId el Message-ID del email.
     * @param username  el nombre del usuario quien consulta.
     * @return el email dentro o vacío si no se encontró.
     */
    Optional<Mail> loadEmail(@Nonnull String messageId, @Nonnull String username);

}
