package whereQR.project.domain.file;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import whereQR.project.domain.file.dto.FileResponseDto;
import whereQR.project.domain.member.Member;
import whereQR.project.exception.CustomExceptions.InternalException;
import whereQR.project.properties.NcsProperties;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final NcsProperties ncsProperties;
    private final AmazonS3 amazonS3;

    public File getFileById(UUID id){
        return fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당하는 File이 존재하지 않습니다.") );
    }

    @Transactional
    public List<File> uploadFile(Member member, String filePath, List<MultipartFile> files){

        List<File> result = new ArrayList<>();

        for(MultipartFile multipartFile: files){
            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getFileName(originalFileName);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            String uploadFileUrl = filePath;

            try (InputStream inputStream = multipartFile.getInputStream()) {

                String fileName = uploadFileName;

                // S3에 폴더 및 파일 업로드
                amazonS3.putObject(
                        new PutObjectRequest(ncsProperties.getBucketName(), fileName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                // S3에 업로드한 폴더 및 파일 URL
                uploadFileUrl = ncsProperties.getEndPoint() + "/" +  ncsProperties.getBucketName() + "/" + uploadFileName;

                File file = new File(uploadFileUrl, member);
                result.add(file);
                fileRepository.save(file);
            } catch (IOException e) {
                throw new InternalException(e.getMessage(), this.getClass().toString());
            }
        }
        return result;
    }

    public String getFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

}
