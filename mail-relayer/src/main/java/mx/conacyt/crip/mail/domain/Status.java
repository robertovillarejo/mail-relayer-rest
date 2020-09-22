package mx.conacyt.crip.mail.domain;

/**
 * Estado de procesamiento de un {@code Mail}.
 */
public enum Status {

    /**
     * Correo encolado.
     *
     * Aún no se ha enviado.
     */
    QUEUED,

    /**
     * Correo enviado exitosamente.
     */
    SENT,

    /**
     * Falló al enviarse.
     */
    FAILED

}
