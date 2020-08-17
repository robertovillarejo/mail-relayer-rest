package mx.conacyt.crip.mail.application.port.in;

import java.util.Objects;

import org.simplejavamail.api.email.Email;

import lombok.Getter;

@Getter
public class SendMailCommand {

    private final Email email;

    public SendMailCommand(Email email) {
        Objects.requireNonNull(email.getFromRecipient());
        Objects.requireNonNull(email.getRecipients());
        this.email = email;
    }

}
