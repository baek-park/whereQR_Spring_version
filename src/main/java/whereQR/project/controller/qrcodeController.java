package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.QrcodeRegisterDto;
import whereQR.project.entity.dto.QrcodeResponseDto;
import whereQR.project.entity.dto.QrcodeScanDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.service.QrcodeService;

import java.util.HashMap;
import java.util.UUID;

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
