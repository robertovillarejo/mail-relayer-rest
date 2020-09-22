package mx.conacyt.crip.mail.adapter.in.web;

import static mx.conacyt.crip.mail.security.AuthoritiesConstants.USER;

import java.net.URI;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.in.GetMailQuery;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase.SendMailCommand;
import mx.conacyt.crip.mail.domain.Mail;
import mx.conacyt.crip.mail.security.SecurityUtils;
import mx.conacyt.crip.mail.web.api.EmailsApiDelegate;
import mx.conacyt.crip.mail.web.model.EmailDto;
import mx.conacyt.crip.mail.web.rest.EmailDtoMapper;

@Secured(USER)
@Component
public class EmailResource implements EmailsApiDelegate {

    private static final String MAIL_STATUS_HEADER = "X-Email-Status";

    @Autowired
    private SendMailUseCase sendMailUseCase;

    @Autowired
    private GetMailQuery getMailQuery;

    @Override
    public ResponseEntity<Void> sendEmail(EmailDto email, Boolean async) {
        if (!Boolean.TRUE.equals(async)) {
            return sendEmail(email);
        }
        return sendEmailAsync(email);
    }

    @Override
    public ResponseEntity<Void> getEmailStatusById(String id) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        Optional<Mail> maybeMail = getMailQuery.getMailById(id, userLogin);
        if (!maybeMail.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().headers(mailStatusHeader(maybeMail.get())).build();
    }

    @Override
    public ResponseEntity<EmailDto> getEmailById(String id) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        Optional<Mail> maybeMail = getMailQuery.getMailById(id, userLogin);
        if (!maybeMail.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().headers(mailStatusHeader(maybeMail.get())).body(EmailDtoMapper.map(maybeMail.get()));
    }

    private ResponseEntity<Void> sendEmailAsync(EmailDto dto) {
        String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        String msgId = sendMailUseCase.sendMail(new SendMailCommand(EmailDtoMapper.map(dto), userLogin, Boolean.TRUE));
        return ResponseEntity.accepted().location(toUri(msgId)).build();
    }

    public ResponseEntity<Void> sendEmail(EmailDto dto) {
        String msgId = sendMailUseCase.sendMail(new SendMailCommand(EmailDtoMapper.map(dto),
                SecurityUtils.getCurrentUserLogin().orElseThrow(), Boolean.FALSE));
        return ResponseEntity.created(toUri(msgId)).build();
    }

    private URI toUri(String msgId) {
        return URI.create(String.format("mid:%s", msgId.replace("<", "").replace(">", "")));
    }

    private HttpHeaders mailStatusHeader(@NotNull Mail mail) {
        if (mail.getStatus() == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(MAIL_STATUS_HEADER, mail.getStatus().toString());
        return headers;
    }

}
