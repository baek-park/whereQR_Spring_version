package whereQR.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000", // local,
                        "http://27.96.130.127:80", // dev,
                        "http://27.96.130.127",
                        "https://where-qr.com"
                )
                .allowedMethods("*")
                .allowCredentials(false)
                .maxAge(3000);
    }
}