package mx.conacyt.crip.mail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;

@Component
public class MailAcknowledgerLogger implements EmailAcknowledger {

    private final Logger log = LoggerFactory.getLogger(MailAcknowledgerLogger.class);

    @Override
    public void success(String msgId) {
        log.debug("Correo {} enviado", msgId);
    }

    @Override
    public void fail(String msgId, Exception e) {
        log.warn("Falló envío de correo {}", msgId, e);
    }
}
