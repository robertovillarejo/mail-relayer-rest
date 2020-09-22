package mx.conacyt.crip.mail.domain;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * Un correo electrónico.
 */
public class Mail extends Email {

    /**
     *
     */
    private static final long serialVersionUID = 8457049161772476543L;

    @Setter
    @Getter
    private Status status;

    public Mail(EmailPopulatingBuilder emailBuilder) {
        super(emailBuilder);
    }

    public Mail withStatus(Status stat) {
        this.status = stat;
        return this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

}
