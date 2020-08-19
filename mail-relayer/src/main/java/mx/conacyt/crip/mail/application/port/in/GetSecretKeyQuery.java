package mx.conacyt.crip.mail.application.port.in;

import java.util.Optional;

import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * La consulta para obtener una {@link SecretKey}.
 */
public interface GetSecretKeyQuery {

    /**
     * Obtiene la {@link SecretKey} asociada al {@link User} con username.
     *
     * @param username el nombre del usuario.
     * @throws UserNotExists si no existe usuario con username.
     * @return la secretKey del usuario.
     */
    Optional<SecretKey> getSecretKey(String username);

}
