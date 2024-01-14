package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.qrcode.QrcodeRegisterDto;
import whereQR.project.entity.dto.qrcode.QrcodeResponseDto;
import whereQR.project.entity.dto.qrcode.QrcodeScanDto;
import whereQR.project.entity.dto.qrcode.QrcodeUpdateDto;
import whereQR.project.service.QrcodeService;
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
    public Qrcode make() throws WriterException {
        return qrcodeService.createQrcode();
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestParam UUID id, @RequestBody QrcodeUpdateDto qrcodeUpdateDto){
        Member currentMember = getMember();
        QrcodeResponseDto qrcodeResponse =  qrcodeService.updateQrcodeByMember(id, currentMember.getId(),  qrcodeUpdateDto);

        return ResponseEntity.builder()
                .status(Status.SUCCESS)
                .data(qrcodeResponse)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestParam UUID id, @RequestBody QrcodeRegisterDto qrcodeRegisterDto){
        QrcodeResponseDto qrcodeResponse= qrcodeService.registerQrcode(id, qrcodeRegisterDto);
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
