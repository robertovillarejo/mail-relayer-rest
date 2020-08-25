package mx.conacyt.crip.mail.adapter.in.web;

import static mx.conacyt.crip.mail.security.AuthoritiesConstants.ADMIN;
import static mx.conacyt.crip.mail.security.AuthoritiesConstants.USER;

import java.net.URI;
import java.util.Base64;
import java.util.stream.Collectors;

import javax.mail.util.ByteArrayDataSource;

import org.simplejavamail.api.email.AttachmentResource;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.security.SecurityUtils;
import mx.conacyt.crip.mail.web.api.EmailsApiDelegate;
import mx.conacyt.crip.mail.web.model.Email;

@Secured({ USER, ADMIN })
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
        String msgId = sendMailUseCase.sendMail(
                new SendMailCommand(map(email), SecurityUtils.getCurrentUserLogin().orElseThrow(), Boolean.TRUE));
        return ResponseEntity.accepted().location(toUri(msgId)).build();
    }

    public ResponseEntity<Void> sendEmail(Email email) {
        String msgId = sendMailUseCase.sendMail(
                new SendMailCommand(map(email), SecurityUtils.getCurrentUserLogin().orElseThrow(), Boolean.FALSE));
        return ResponseEntity.created(toUri(msgId)).build();
    }

    private org.simplejavamail.api.email.Email map(Email email) {
        // @formatter:off
        EmailPopulatingBuilder builder =
            EmailBuilder.startingBlank()
                .toMultiple(email.getTo())
                .from(email.getSender())
                .withSubject(email.getSubject());
        // @formatter:on
        if (email.getCc() != null) {
            builder.ccAddresses(email.getCc());
        }
        if (email.getBcc() != null) {
            builder.bccAddresses(email.getBcc());
        }
        if (email.getReplyTo() != null) {
            builder.withReplyTo(email.getReplyTo());
        }
        if (email.getPlainBody() != null) {
            builder.withPlainText(email.getPlainBody());
        }
        if (email.getHtmlBody() != null) {
            builder.withHTMLText(email.getHtmlBody());
        }
        if (email.getAttachments() != null) {
            builder.withAttachments(email.getAttachments().stream()
                    .map(a -> new AttachmentResource(a.getFilename(),
                            new ByteArrayDataSource(Base64.getDecoder().decode(a.getData()), a.getContentType())))
                    .collect(Collectors.toList()));
        }
        return builder.buildEmail();
    }

    private URI toUri(String msgId) {
        return URI.create(String.format("mid:%s", msgId.replace("<", "").replace(">", "")));
    }
}
