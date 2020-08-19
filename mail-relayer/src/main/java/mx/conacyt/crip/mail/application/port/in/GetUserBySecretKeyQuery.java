package mx.conacyt.crip.mail.application.port.in;

import java.util.Optional;

import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * La consulta para obtener el {@link User} propietario de la {@link SecretKey}.
 */
public interface GetUserBySecretKeyQuery {

    /**
     * Obtiene el {@link User} propietario de la {@link SecretKey}.
     *
     * @param secretKey la secretKey asociada al usuario.
     * @return el usuario propietario de la secretKey o vacío.
     */
    Optional<User> getUserBySecretKey(SecretKey secretKey);

}
