package mx.conacyt.crip.mail.domain;

import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "emails")
public class EmailMongoEntity {

    @Id
    private String id;

    @DBRef
    private UserMongoEntity user;

    private boolean sent;

    private String status;

    private List<String> to;

    private String subject;

    @Field("plain_body")
    private String plainBody;

    private String sender;

    @Field("html_body")
    private String htmlBody;

    public EmailMongoEntity() {
    }

    public EmailMongoEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EmailMongoEntity id(String id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EmailMongoEntity)) {
            return false;
        }
        EmailMongoEntity emailMongoEntity = (EmailMongoEntity) o;
        return Objects.equals(id, emailMongoEntity.id);
    }

    public EmailMongoEntity(String id, UserMongoEntity user) {
        this.id = id;
        this.user = user;
    }

    public UserMongoEntity getUser() {
        return this.user;
    }

    public void setUser(UserMongoEntity user) {
        this.user = user;
    }

    public EmailMongoEntity user(UserMongoEntity user) {
        this.user = user;
        return this;
    }

    public boolean isSent() {
        return this.sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public EmailMongoEntity sent(boolean sent) {
        this.sent = sent;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmailMongoEntity status(String status) {
        this.status = status;
        return this;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public EmailMongoEntity to(List<String> to) {
        this.to = to;
        return this;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPlainBody() {
        return this.plainBody;
    }

    public void setPlainBody(String plainBody) {
        this.plainBody = plainBody;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getHtmlBody() {
        return this.htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public EmailMongoEntity subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailMongoEntity plainBody(String plainBody) {
        this.plainBody = plainBody;
        return this;
    }

    public EmailMongoEntity sender(String sender) {
        this.sender = sender;
        return this;
    }

    public EmailMongoEntity htmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + "}";
    }

}
