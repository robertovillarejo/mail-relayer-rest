package mx.conacyt.crip.mail.web.rest;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.simplejavamail.api.email.AttachmentResource;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.email.Recipient;
import org.simplejavamail.email.EmailBuilder;

import mx.conacyt.crip.mail.web.model.AttachmentDto;
import mx.conacyt.crip.mail.web.model.EmailDto;

public class EmailDtoMapper {

    private EmailDtoMapper() {
    }

    public static org.simplejavamail.api.email.Email map(EmailDto email) {
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

    /**
     * Mapea un {@code org.simplejavamail.api.email.Email} a {@code Email}.
     */
    public static EmailDto map(org.simplejavamail.api.email.Email email) {
        List<String> toAddresses = filterRecipientByType(email, javax.mail.Message.RecipientType.TO);
        List<String> recipientCc = filterRecipientByType(email, javax.mail.Message.RecipientType.CC);
        List<String> recipientBcc = filterRecipientByType(email, javax.mail.Message.RecipientType.BCC);
        List<AttachmentDto> attachments = email.getAttachments().stream().map(EmailDtoMapper::map)
                .collect(Collectors.toList());
        EmailDto dto = new EmailDto();
        dto.to(toAddresses).subject(email.getSubject()).plainBody(email.getPlainText())
                .sender(email.getFromRecipient().getAddress()).htmlBody(email.getHTMLText());
        if (recipientCc != null) {
            dto.cc(recipientCc);
        }
        if (recipientBcc != null) {
            dto.bcc(recipientBcc);
        }
        if (email.getReplyToRecipient() != null) {
            dto.replyTo(email.getReplyToRecipient().getAddress());
        }
        if (!email.getAttachments().isEmpty()) {
            dto.attachments(attachments);
        }
        return dto;
    }

    /**
     * Mapea un {@code AttachmentResource} a {@code AttachmentDto}.
     *
     * @param attachment
     * @return
     */
    public static AttachmentDto map(AttachmentResource attachment) {
        String data;
        try {
            data = Base64.getEncoder().encodeToString(IOUtils.toByteArray(attachment.getDataSource().getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("No se pudo codificar el adjunto %s a base 64", attachment.getName()), e);
        }
        return new AttachmentDto().filename(attachment.getName())
                .contentType(attachment.getDataSource().getContentType()).data(data);
    }

    private static List<String> filterRecipientByType(org.simplejavamail.api.email.Email email,
            javax.mail.Message.RecipientType type) {
        return email.getRecipients().stream().filter(r -> type.toString().equals(r.getType().toString()))
                .map(Recipient::getAddress).collect(Collectors.toList());
    }

}
