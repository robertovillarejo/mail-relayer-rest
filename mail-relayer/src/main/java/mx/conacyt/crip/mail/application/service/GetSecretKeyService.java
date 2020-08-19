package mx.conacyt.crip.mail.application.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.application.port.in.GetSecretKeyQuery;
import mx.conacyt.crip.mail.application.port.out.LoadSecretKeyPort;
import mx.conacyt.crip.mail.domain.SecretKey;

/**
 * Implementación de la consulta para obtener una secretKey.
 */
@RequiredArgsConstructor
public class GetSecretKeyService implements GetSecretKeyQuery {

    private final LoadSecretKeyPort loadSecretPort;

    @Override
    public Optional<SecretKey> getSecretKey(String username) {
        return loadSecretPort.loadSecretKey(username);
    }

}
