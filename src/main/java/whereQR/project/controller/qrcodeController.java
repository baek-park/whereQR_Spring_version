package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Member;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.Role;
import whereQR.project.entity.dto.qrcode.QrcodeRegisterDto;
import whereQR.project.entity.dto.qrcode.QrcodeResponseDto;
import whereQR.project.entity.dto.qrcode.QrcodeScanDto;
import whereQR.project.entity.dto.qrcode.QrcodeUpdateDto;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.service.QrcodeService;

import java.util.HashMap;
import java.util.UUID;

import static whereQR.project.utils.MemberUtil.getMember;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Slf4j
public class qrcodeController {

    private final QrcodeService qrcodeService;
    @PostMapping("/create")
    public Qrcode make() throws WriterException {

        Member currentMember = getMember();

        if(currentMember.getRole() == Role.USER){
            throw new ForbiddenException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
        }

        return qrcodeService.createQrcode();
    }

    @PostMapping("/update")
    public QrcodeResponseDto update(@RequestParam UUID id, @RequestBody QrcodeUpdateDto qrcodeUpdateDto){
        return qrcodeService.updateQrcode(id, qrcodeUpdateDto);
    }

    @PostMapping("/register")
    public QrcodeResponseDto register(@RequestParam UUID id, @RequestBody QrcodeRegisterDto qrcodeRegisterDto){
        return qrcodeService.registerQrcode(id, qrcodeRegisterDto);
    }

    @GetMapping("/scan")
    public QrcodeScanDto scan(@RequestParam UUID id){
        return qrcodeService.getQrcode(id);
    }

    @GetMapping("/qrcode-list")
    public HashMap<String, Object> getQrList(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("data", qrcodeService.getQrcodeByMember());
        return map;
    }
}
