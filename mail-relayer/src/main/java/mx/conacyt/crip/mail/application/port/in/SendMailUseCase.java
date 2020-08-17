package mx.conacyt.crip.mail.application.port.in;

public interface SendMailUseCase {

    void sendMail(SendMailCommand command);

}
