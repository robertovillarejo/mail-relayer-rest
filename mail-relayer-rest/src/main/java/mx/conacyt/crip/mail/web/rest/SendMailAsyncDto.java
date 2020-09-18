package mx.conacyt.crip.mail.web.rest;

import java.io.Serializable;

import mx.conacyt.crip.mail.web.model.EmailDto;

public class SendMailAsyncDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4994485474706136460L;

    private final EmailDto email;
    private final String messageId;
    private final String userLogin;

    public SendMailAsyncDto(EmailDto email, String messageId, String userLogin) {
        this.email = email;
        this.messageId = messageId;
        this.userLogin = userLogin;
    }

    public SendMailAsyncDto() {
        this.email = null;
        this.messageId = null;
        this.userLogin = null;
    }

    public EmailDto getEmail() {
        return email;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getUserLogin() {
        return userLogin;
    }

}
