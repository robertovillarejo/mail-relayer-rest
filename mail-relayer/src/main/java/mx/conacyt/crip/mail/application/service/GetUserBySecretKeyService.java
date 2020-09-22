package mx.conacyt.crip.mail.application.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.application.port.in.GetUserBySecretKeyQuery;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.domain.SecretKey;
import mx.conacyt.crip.mail.domain.User;

/**
 * Implementación de la consulta para obtener el usuario propietario de una
 * secretKey.
 */
@RequiredArgsConstructor
class GetUserBySecretKeyService implements GetUserBySecretKeyQuery {

    private final LoadUserPort loadUserPort;

    @Override
    public Optional<User> getUserBySecretKey(SecretKey secretKey) {
        return loadUserPort.loadUserBySecretKey(secretKey);
    }

}
