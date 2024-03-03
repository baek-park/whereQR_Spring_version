package whereQR.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "auth")
public final class AuthProperties {
    private Boolean secure;
    private Boolean httpOnly;

    private String domain;
}
