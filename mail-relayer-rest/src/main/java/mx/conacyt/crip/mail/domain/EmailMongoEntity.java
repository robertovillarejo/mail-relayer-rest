package mx.conacyt.crip.mail.domain;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "emails")
public class EmailMongoEntity {

    @Id
    private String id;

    @DBRef
    private UserMongoEntity user;

    private boolean sent;

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

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + "}";
    }

}
