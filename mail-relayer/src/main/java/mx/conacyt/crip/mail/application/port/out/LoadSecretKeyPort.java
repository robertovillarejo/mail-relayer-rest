package mx.conacyt.crip.mail.application.port.out;

import java.util.Optional;

import mx.conacyt.crip.mail.domain.SecretKey;

/**
 * Puerto para cargar una {@link SecretKey}.
 */
public interface LoadSecretKeyPort {

    /**
     * Carga la secretKey del usuario con username.
     *
     * @param username el nombre de usuario.
     * @return la secretKey | vacío si no se encontró.
     */
    Optional<SecretKey> loadSecretKey(String username);

}
