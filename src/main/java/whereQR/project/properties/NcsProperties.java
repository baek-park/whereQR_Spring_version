package whereQR.project.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.ncp")
public class NcsProperties {
    private String endPoint;
    private String accessKey;
    private String secretKey;
    private String regionName;
}
