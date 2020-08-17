package mx.conacyt.crip.mail.adapter.in.web;

import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.web.api.EmailsApiDelegate;
import mx.conacyt.crip.mail.web.model.Email;

@Component
public class EmailResource implements EmailsApiDelegate {

    @Autowired
    private SendMailUseCase sendMailUseCase;

    @Override
    public ResponseEntity<Void> sendEmail(Email email) {
        sendMailUseCase.sendMail(
                new SendMailCommand(EmailBuilder.startingBlank().toMultiple(email.getTo()).from(email.getFrom())
                        .withSubject(email.getSubject()).withPlainText(email.getPlainBody()).buildEmail()));
        return ResponseEntity.ok().build();
    }

}
