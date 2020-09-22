package mx.conacyt.crip.mail.application.service;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mx.conacyt.crip.mail.application.port.in.GenerateSecretKeyUseCase;
import mx.conacyt.crip.mail.application.port.in.GetMailQuery;
import mx.conacyt.crip.mail.application.port.in.GetSecretKeyQuery;
import mx.conacyt.crip.mail.application.port.in.GetUserBySecretKeyQuery;
import mx.conacyt.crip.mail.application.port.in.RegisterUserUseCase;
import mx.conacyt.crip.mail.application.port.in.SendMailUseCase;
import mx.conacyt.crip.mail.application.port.out.CreateUserPort;
import mx.conacyt.crip.mail.application.port.out.EmailAcknowledger;
import mx.conacyt.crip.mail.application.port.out.LoadEmailPort;
import mx.conacyt.crip.mail.application.port.out.LoadSecretKeyPort;
import mx.conacyt.crip.mail.application.port.out.LoadUserPort;
import mx.conacyt.crip.mail.application.port.out.SaveEmailPort;
import mx.conacyt.crip.mail.application.port.out.SaveSecretKeyPort;
import mx.conacyt.crip.mail.config.ApplicationProperties;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Mailer mailer(ApplicationProperties props) {
        return MailerBuilder.withSMTPServer(props.getRelayHost(), props.getRelayPort()).buildMailer();
    }

    @Bean
    public SendMailUseCase sendMailUseCase(Mailer mailer, EmailAcknowledger acknowledger, LoadUserPort loadUserPort,
            SaveEmailPort saveEmailPort) {
        return new SendMailService(mailer, saveEmailPort, loadUserPort, acknowledger);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(CreateUserPort createUserPort, LoadUserPort loadUserPort) {
        return new RegisterUserService(createUserPort, loadUserPort);
    }

    @Bean
    public GenerateSecretKeyUseCase generateSecretKeyUseCase(SaveSecretKeyPort saveSecretKeyPort,
            LoadUserPort loadUserPort) {
        return new GenerateSecretKeyService(saveSecretKeyPort, loadUserPort);
    }

    @Bean
    public GetUserBySecretKeyQuery getUserBySecretKeyService(LoadUserPort loadUserPort) {
        return new GetUserBySecretKeyService(loadUserPort);
    }

    @Bean
    public GetSecretKeyQuery getSecretKeyQuery(LoadSecretKeyPort loadSecretKeyPort) {
        return new GetSecretKeyService(loadSecretKeyPort);
    }

    @Bean
    public GetMailQuery getMailQuery(LoadEmailPort loadEmailPort, LoadUserPort loadUserPort) {
        return new GetMailService(loadEmailPort, loadUserPort);
    }

}
