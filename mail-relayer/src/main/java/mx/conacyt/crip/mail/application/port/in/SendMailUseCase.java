package mx.conacyt.crip.mail.application.port.in;

public interface SendMailUseCase {

    String sendMail(SendMailCommand command);

}
