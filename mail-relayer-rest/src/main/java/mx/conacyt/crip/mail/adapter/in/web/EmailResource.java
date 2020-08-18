package mx.conacyt.crip.mail.adapter.in.web;

import java.net.URI;

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
    public ResponseEntity<Void> sendEmail(Email email, Boolean async) {
        if (!Boolean.TRUE.equals(async)) {
            return sendEmail(email);
        }
        return sendEmailAsync(email);
    }

    private ResponseEntity<Void> sendEmailAsync(Email email) {
        String msgId = sendMailUseCase.sendMail(new SendMailCommand(map(email), Boolean.TRUE));
        return ResponseEntity.accepted().location(toUri(msgId)).build();
    }

    public ResponseEntity<Void> sendEmail(Email email) {
        String msgId = sendMailUseCase.sendMail(new SendMailCommand(map(email), Boolean.FALSE));
        return ResponseEntity.created(toUri(msgId)).build();
    }

    private org.simplejavamail.api.email.Email map(Email email) {
        return EmailBuilder.startingBlank().toMultiple(email.getTo()).from(email.getFrom())
                .withSubject(email.getSubject()).withPlainText(email.getPlainBody()).buildEmail();
    }

    private URI toUri(String msgId) {
        return URI.create(String.format("mid:%s", msgId.replace("<", "").replace(">", "")));
    }
}
