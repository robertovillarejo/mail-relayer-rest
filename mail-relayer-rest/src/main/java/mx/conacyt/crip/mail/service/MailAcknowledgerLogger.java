package mx.conacyt.crip.mail.service;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;

@Slf4j
@Component
public class MailAcknowledgerLogger implements EmailAcknowledger {

    @Override
    public void success(String msgId) {
        log.debug("Correo {} enviado", msgId);
    }

    @Override
    public void fail(String msgId, Exception e) {
        log.warn("Falló envío de correo {}", msgId, e);
    }
}
