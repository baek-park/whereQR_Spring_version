package whereQR.project.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import whereQR.project.properties.EmailAuthProperties;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(EmailAuthProperties.class)
@Slf4j
public class EmailConfig {
    @Bean
    public JavaMailSender javaMailSender(EmailAuthProperties emailAuthProperties) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailAuthProperties.getHost());
        mailSender.setPort(emailAuthProperties.getPort());
        mailSender.setUsername(emailAuthProperties.getUsername());
        mailSender.setPassword(emailAuthProperties.getPassword());
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties(emailAuthProperties));
        return mailSender;
    }

    @Bean
    public Properties getMailProperties(EmailAuthProperties emailAuthProperties) {
//        EmailAuthProperties.Smtp smtpProperties = emailAuthProperties.getSmtp();
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", emailAuthProperties.isAuth());
        properties.put("mail.smtp.starttls.enable", emailAuthProperties.isEnable());
        properties.put("mail.smtp.starttls.required", emailAuthProperties.isRequired());
        properties.put("mail.smtp.connectiontimeout", emailAuthProperties.getConnectionTimeout());
        properties.put("mail.smtp.timeout", emailAuthProperties.getTimeout());
        properties.put("mail.smtp.writetimeout", emailAuthProperties.getWriteTimeout());
        return properties;
    }

}
