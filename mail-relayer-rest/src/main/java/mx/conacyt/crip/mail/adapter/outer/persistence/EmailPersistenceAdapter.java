package mx.conacyt.crip.mail.adapter.outer.persistence;

import java.util.Optional;

import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.out.LoadEmailPort;
import mx.conacyt.crip.mail.application.port.out.SaveEmailPort;
import mx.conacyt.crip.mail.domain.EmailMongoEntity;
import mx.conacyt.crip.mail.domain.Mail;
import mx.conacyt.crip.mail.domain.Status;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.EmailRepository;
import mx.conacyt.crip.mail.repository.UserRepository;
import mx.conacyt.crip.mail.web.rest.EmailDtoMapper;

@Component
public class EmailPersistenceAdapter implements SaveEmailPort, LoadEmailPort {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    public EmailPersistenceAdapter(EmailRepository emailRepository, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveEmail(Mail mail, String username) {
        UserMongoEntity user = userRepository.findByName(username).orElse(null);
        emailRepository.save(new EmailMongoEntity().id(mail.getId()).user(user).sent(mail.getSentDate() != null)
                .to(EmailDtoMapper.filterRecipientByType(mail, javax.mail.Message.RecipientType.TO))
                .subject(mail.getSubject()).sender(mail.getFromRecipient().getAddress()).plainBody(mail.getPlainText())
                .htmlBody(mail.getHTMLText()).status(mail.getStatus().toString()));
    }

    @Override
    public Optional<Mail> loadEmail(String messageId, String username) {
        Optional<EmailMongoEntity> maybeEntity = emailRepository.findByIdAndUser(messageId, username);
        if (maybeEntity.isEmpty()) {
            return Optional.empty();
        }
        EmailMongoEntity entity = maybeEntity.get();
        EmailPopulatingBuilder emailBuilder = EmailBuilder.startingBlank().fixingMessageId(entity.getId())
                .toMultiple(entity.getTo()).withSubject(entity.getSubject()).from(entity.getSender())
                .withPlainText(entity.getPlainBody()).withHTMLText(entity.getHtmlBody());
        return Optional.of(new Mail(emailBuilder).withStatus(Status.valueOf(entity.getStatus())));
    }

}
