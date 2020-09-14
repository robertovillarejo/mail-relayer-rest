package mx.conacyt.crip.mail.adapter.outer.persistence;

import org.simplejavamail.api.email.Email;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.port.out.SaveEmailPort;
import mx.conacyt.crip.mail.domain.EmailMongoEntity;
import mx.conacyt.crip.mail.domain.UserMongoEntity;
import mx.conacyt.crip.mail.repository.EmailRepository;
import mx.conacyt.crip.mail.repository.UserRepository;

@Component
public class EmailPersistenceAdapter implements SaveEmailPort {

    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    public EmailPersistenceAdapter(EmailRepository emailRepository, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveEmail(Email email, String username) {
        UserMongoEntity user = userRepository.findByName(username).orElse(null);
        emailRepository.save(new EmailMongoEntity().id(email.getId()).user(user).sent(email.getSentDate() != null));
    }

}
