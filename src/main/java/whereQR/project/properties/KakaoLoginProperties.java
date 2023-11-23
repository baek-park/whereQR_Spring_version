package whereQR.project.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao")
public final class KakaoLoginProperties {

    private String tokenRequestUrl;
    private String userDataRequestUrl;
    private String grantType;
    private String clientId;
    private String redirectUrl;

}
