package whereQR.project.domain.qrcode;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.qrcode.dto.QrcodeRegisterDto;
import whereQR.project.domain.qrcode.dto.QrcodeResponseDto;
import whereQR.project.domain.qrcode.dto.QrcodeScanDto;
import whereQR.project.domain.qrcode.dto.QrcodeUpdateDto;
import whereQR.project.utils.response.ResponseEntity;
import whereQR.project.utils.response.Status;

import java.util.*;
import static whereQR.project.utils.MemberUtil.getMember;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Slf4j
public class QrcodeController {

    private final QrcodeService qrcodeService;
    @PostMapping("/create")
    public void make() throws WriterException {

        for(int i=0; i<150; i++){
            qrcodeService.createQrcode();
        }
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody QrcodeUpdateDto qrcodeUpdateDto){
        Member currentMember = getMember();
        QrcodeResponseDto qrcodeResponse =  qrcodeService.updateQrcodeByMember(qrcodeUpdateDto.getId(), currentMember.getId(),  qrcodeUpdateDto);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(qrcodeResponse)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody QrcodeRegisterDto qrcodeRegisterDto){
        QrcodeResponseDto qrcodeResponse= qrcodeService.registerQrcode(qrcodeRegisterDto.getId(), qrcodeRegisterDto);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(qrcodeResponse)
                .build();
    }

    @GetMapping("/scan")
    public ResponseEntity scan(@RequestParam UUID id){
        QrcodeScanDto qrcodeScanDto = qrcodeService.getQrcode(id);
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(qrcodeScanDto)
                .build();
    }

    @GetMapping("/my")
    public ResponseEntity getQrList(){
        Member member = getMember();
        List<QrcodeResponseDto> memberQrcodes = qrcodeService.getQrcodeByMember(member.getId());
        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(memberQrcodes)
                .build();
    }
}
