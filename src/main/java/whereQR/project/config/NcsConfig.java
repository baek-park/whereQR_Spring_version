package whereQR.project.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import whereQR.project.properties.NcsProperties;

@Configuration
@EnableConfigurationProperties(NcsProperties.class)
public class NcsConfig {

    @Bean
    public AmazonS3 amazonS3(NcsProperties ncsProperties) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ncsProperties.getAccessKey(), ncsProperties.getSecretKey());

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ncsProperties.getEndPoint(), ncsProperties.getRegionName()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
