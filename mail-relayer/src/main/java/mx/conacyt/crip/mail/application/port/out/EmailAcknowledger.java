package mx.conacyt.crip.mail.application.port.out;

/**
 * Acusador de éxito/fallo de envío de un email.
 */
public interface EmailAcknowledger {

    /**
     * Notifica éxito al enviar email con msgId.
     *
     * @param msgId el id del email que ya se envió.
     */
    void success(String msgId);

    /**
     * Notifica fallo al enviar email con msgId.
     *
     * @param msgId el id del email que falló en enviar.
     * @param e     la excepción causante del fallo.
     */
    void fail(String msgId, Exception e);

}
