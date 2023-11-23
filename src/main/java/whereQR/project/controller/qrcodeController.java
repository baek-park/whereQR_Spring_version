package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.qrcode.QrcodeRegisterDto;
import whereQR.project.entity.dto.qrcode.QrcodeResponseDto;
import whereQR.project.entity.dto.qrcode.QrcodeScanDto;
import whereQR.project.entity.dto.qrcode.QrcodeUpdateDto;
import whereQR.project.service.QrcodeService;

import java.util.*;
import static whereQR.project.utils.MemberUtil.getMember;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Slf4j
public class qrcodeController {

    private final QrcodeService qrcodeService;
    @PostMapping("/create")
    public Qrcode make() throws WriterException {
        return qrcodeService.createQrcode();
    }

    @PostMapping("/update")
    public ResponseEntity<QrcodeResponseDto> update(@RequestParam UUID id, @RequestBody QrcodeUpdateDto qrcodeUpdateDto){
        Member currentMember = getMember();
        QrcodeResponseDto qrcodeResponse =  qrcodeService.updateQrcodeByMember(id, currentMember.getId(),  qrcodeUpdateDto);
        return ResponseEntity.ok(qrcodeResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<QrcodeResponseDto> register(@RequestParam UUID id, @RequestBody QrcodeRegisterDto qrcodeRegisterDto){
        QrcodeResponseDto qrcodeResponse= qrcodeService.registerQrcode(id, qrcodeRegisterDto);
        return ResponseEntity.ok(qrcodeResponse);
    }

    @GetMapping("/scan")
    public ResponseEntity<QrcodeScanDto> scan(@RequestParam UUID id){
        QrcodeScanDto qrcodeScan = qrcodeService.getQrcode(id);
        return ResponseEntity.ok(qrcodeScan);
    }

    @GetMapping("/my")
    public ResponseEntity<List<QrcodeResponseDto>> getQrList(){
        Member member = getMember();
        List<QrcodeResponseDto> memberQrcodes = qrcodeService.getQrcodeByMember(member.getId());
        return ResponseEntity.ok(memberQrcodes);
    }
}
