package whereQR.project.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import whereQR.project.properties.AuthProperties;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfig {

}
