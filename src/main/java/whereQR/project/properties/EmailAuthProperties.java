package whereQR.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.email")
public final class EmailAuthProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private int authCodeExpirationMillis;
    private boolean auth;
    private int connectionTimeout;
    private int timeout;
    private int writeTimeout;
    boolean enable;
    boolean required;

}
