package mx.conacyt.crip.mail.application.port.out;

import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * Puerto para guardar la {@link SecretKey}.
 */
public interface SaveSecretKeyPort {

    /**
     * Guarda la secretKey y la asocia al user.
     *
     * @param secretKey la secretKey a guardar.
     * @param user      el usuario propietario de la secretKey.
     */
    void saveSecretKey(SecretKey secretKey, User user);

}
