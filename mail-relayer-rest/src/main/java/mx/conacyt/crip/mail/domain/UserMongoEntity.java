package mx.conacyt.crip.mail.domain;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import mx.conacyt.crip.mail.config.Constants;

/**
 * A user.
 */
@Document(collection = "users")
public class UserMongoEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String name;

    public String getName() {
        return name;
    }

    // Lowercase the login before saving it in database
    public void setName(String name) {
        this.name = StringUtils.lowerCase(name);
    }

    public UserMongoEntity name(String name) {
        setName(name);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserMongoEntity)) {
            return false;
        }
        UserMongoEntity userMongoEntity = (UserMongoEntity) o;
        return Objects.equals(name, userMongoEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
