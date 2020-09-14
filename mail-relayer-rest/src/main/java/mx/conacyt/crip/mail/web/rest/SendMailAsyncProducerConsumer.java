package mx.conacyt.crip.mail.web.rest;

import com.google.common.eventbus.Subscribe;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import mx.conacyt.crip.mail.application.service.DomainEventBus;
import mx.conacyt.crip.mail.domain.events.EmailAsyncQueued;
import mx.conacyt.crip.mail.domain.events.EmailAsyncReceived;

@Component
public class SendMailAsyncProducerConsumer {

    private final AmqpTemplate amqpTemplate;

    public SendMailAsyncProducerConsumer(AmqpTemplate amqpTemplate) {
        DomainEventBus.EVENT_BUS.register(this);
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * Redirige el evento {@code EmailAsyncReceived} a la cola de trabajo AMQP.
     *
     * Transforma el evento de dominio a un mensaje AMQP y lo publica.
     *
     * @param event el evento de email recibido.
     */
    @Subscribe
    public void queueMail(EmailAsyncReceived event) {
        mx.conacyt.crip.mail.web.model.Email dto = EmailDtoMapper.map(event.getEmail());
        String msgId = event.getEmail().getId();
        String userLogin = event.getUsername();
        amqpTemplate.convertAndSend("mailRelay", new SendMailAsyncDto(dto, msgId, userLogin));
    }

    /**
     * Recibe mensajes de la cola de trabajo y los publica como eventos de dominio
     * para que puedan ser procesados.
     *
     * @param dto el mensaje AMQP.
     */
    @RabbitListener(queues = "mailRelay", id = "emailConsumer")
    public void processMail(SendMailAsyncDto dto) {
        Email email = EmailBuilder.copying(EmailDtoMapper.map(dto.getEmail())).fixingMessageId(dto.getMessageId())
                .buildEmail();
        DomainEventBus.EVENT_BUS.post(new EmailAsyncQueued(email, dto.getUserLogin()));
    }

}
