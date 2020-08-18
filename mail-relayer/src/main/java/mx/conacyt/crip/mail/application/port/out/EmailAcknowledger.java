package mx.conacyt.crip.mail.application.port.out;

public interface EmailAcknowledger {

    void success(String msgId);

    void fail(String msgId, Exception e);

}
