package whereQR.project.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import whereQR.project.properties.QrcodeProperties;

@Configuration
@EnableConfigurationProperties(QrcodeProperties.class)
public class QrcodeConfig {

}
