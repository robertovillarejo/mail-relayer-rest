package mx.conacyt.crip.mail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Mailrelayer.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String relayHost = "127.0.0.1";

    private int relayPort = 25;

    public String getRelayHost() {
        return relayHost;
    }

    public void setRelayHost(String relayHost) {
        this.relayHost = relayHost;
    }

    public int getRelayPort() {
        return relayPort;
    }

    public void setRelayPort(int relayPort) {
        this.relayPort = relayPort;
    }

}
