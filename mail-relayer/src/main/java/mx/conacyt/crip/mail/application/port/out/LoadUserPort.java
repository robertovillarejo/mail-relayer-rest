package mx.conacyt.crip.mail.application.port.out;

import java.util.Optional;

import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * Puerto para cargar un {@link User}.
 */
public interface LoadUserPort {

    /**
     * Carga el usuario con name proporcionado.
     *
     * @param name el nombre de usuario.
     * @return el usuario | vacío si no se encontró.
     */
    Optional<User> loadUser(String name);

    /**
     * Revisa si existe el usuario con name proporcionado.
     *
     * @param name el nombre de usuario.
     * @return true si existe | false si no existe.
     */
    boolean existsUser(String name);

    /**
     * Carga el usuario propietario de la secretKey proporcionada.
     *
     * @param secretKey la secretKey del usuario.
     * @return el usuario | vacío si no se encontró.
     */
    Optional<User> loadUserBySecretKey(SecretKey secretKey);

}
