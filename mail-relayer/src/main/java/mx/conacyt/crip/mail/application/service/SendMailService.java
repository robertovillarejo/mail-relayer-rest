package mx.conacyt.crip.mail.application.service;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import lombok.RequiredArgsConstructor;
import mx.conacyt.crip.mail.application.port.in.SendMailCommand;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;

@RequiredArgsConstructor
class SendMailService implements SendMailUseCase {

    private final Mailer mailer;

    @Override
    public void sendMail(SendMailCommand command) {
        final Email email = command.getEmail();
        mailer.sendMail(email);
    }

}
