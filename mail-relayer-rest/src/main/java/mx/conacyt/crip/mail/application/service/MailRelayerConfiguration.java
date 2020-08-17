package mx.conacyt.crip.mail.application.service;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.config.ApplicationProperties;

@Configuration
public class MailRelayerConfiguration {

    @Bean
    public Mailer mailer(ApplicationProperties props) {
        return MailerBuilder.withSMTPServer(props.getRelayHost(), props.getRelayPort()).buildMailer();
    }

    @Bean
    public SendMailUseCase sendMailUseCase(Mailer mailer) {
        return new SendMailService(mailer);
    }

}
