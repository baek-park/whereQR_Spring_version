package whereQR.project.utils;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import whereQR.project.domain.file.FileService;
import whereQR.project.properties.NcsProperties;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final FileService fileService;
    private final AmazonS3 amazonS3;
    private final NcsProperties ncsProperties;
    @Scheduled(cron = "0 0 6 * * *") // 매일 6시에 동작
    public void deleteNotUseFileFromStorage(){

        List<String> objectName = fileService.getDeleteFilesFromStorage();

        objectName.forEach(it ->{
            try {
                amazonS3.deleteObject(ncsProperties.getBucketName(), it);
            } catch (AmazonS3Exception e) {
                log.error("file 삭제 에러 {}", e);
            } catch (SdkClientException e) {
                log.error("file 삭제 에러 {}", e);
            }});
    }
}
