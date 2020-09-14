package mx.conacyt.crip.mail.adapter.in.web;

import static mx.conacyt.crip.mail.security.AuthoritiesConstants.USER;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.security.SecurityUtils;
import mx.conacyt.crip.mail.web.api.EmailsApiDelegate;
import mx.conacyt.crip.mail.web.model.Email;
import mx.conacyt.crip.mail.web.rest.EmailDtoMapper;

@Secured(USER)
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

    private ResponseEntity<Void> sendEmailAsync(Email dto) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        String msgId = sendMailUseCase
                .sendMail(new SendMailCommand(EmailDtoMapper.map(dto), userLogin, Boolean.TRUE));
        return ResponseEntity.accepted().location(toUri(msgId)).build();
    }

    public ResponseEntity<Void> sendEmail(Email dto) {
        String msgId = sendMailUseCase.sendMail(new SendMailCommand(EmailDtoMapper.map(dto),
                SecurityUtils.getCurrentUserLogin().orElseThrow(), Boolean.FALSE));
        return ResponseEntity.created(toUri(msgId)).build();
    }

    private URI toUri(String msgId) {
        return URI.create(String.format("mid:%s", msgId.replace("<", "").replace(">", "")));
    }

}
