package whereQR.project.domain.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import whereQR.project.domain.member.Member;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.exception.CustomExceptions.InternalException;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {


    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity createFile(@RequestParam(value = "file", required = false) List<MultipartFile> files){

        Member currentMember = MemberUtil.getMember();

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(fileService.uploadFile(currentMember, "/", files ).stream().map(it -> it.toFileResponseDto()))
                .build();
    }

//    @PostMapping("/delete")
//    public ResponseEntity deleteFile(@RequestParam UUID id) {
//        Member currentMember = MemberUtil.getMember();
//
//        File file = fileService.getFileById(id);
//        if (file.getUploader() != currentMember) {
//            throw new ForbiddenException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
//        }
//
//        fileService.deleteFile(file);
//        return ResponseEntity.builder().
//                status(Status.SUCCESS)
//                .data(id)
//                .build()
//    }

}
