package mx.conacyt.crip.mail.domain;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "secret_keys")
public class SecretKeyMongoEntity {

    @Id
    private String id;

    private String content;

    @DBRef
    private UserMongoEntity user;

    public SecretKeyMongoEntity() {
    }

    public SecretKeyMongoEntity(String id, String content, UserMongoEntity user) {
        this.id = id;
        this.content = content;
        this.user = user;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserMongoEntity getUser() {
        return this.user;
    }

    public void setUser(UserMongoEntity user) {
        this.user = user;
    }

    public SecretKeyMongoEntity id(String id) {
        this.id = id;
        return this;
    }

    public SecretKeyMongoEntity content(String content) {
        this.content = content;
        return this;
    }

    public SecretKeyMongoEntity user(UserMongoEntity user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SecretKeyMongoEntity)) {
            return false;
        }
        SecretKeyMongoEntity secretKeyMongoEntity = (SecretKeyMongoEntity) o;
        return Objects.equals(id, secretKeyMongoEntity.id) && Objects.equals(content, secretKeyMongoEntity.content)
                && Objects.equals(user, secretKeyMongoEntity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, user);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", content='" + getContent() + "'" + ", user='" + getUser() + "'" + "}";
    }

}
