package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.QrcodeScanDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.entity.dto.QrcodeUpdateResponseDto;
import whereQR.project.service.QrcodeService;

import java.util.HashMap;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Slf4j
public class qrcodeController {

    private final QrcodeService qrcodeService;
    @PostMapping("/create")
    public Qrcode makeQr() throws WriterException {
        return qrcodeService.create();
    }

    @PostMapping("/update")
    public QrcodeUpdateResponseDto updateQr(@RequestParam String key, @RequestBody QrcodeUpdateDto qrcodeUpdateDto){
        return qrcodeService.update(key, qrcodeUpdateDto);
    }

    @GetMapping("/scan")
    public QrcodeScanDto scanQr(@RequestParam String key){
        return qrcodeService.scan(key);
    }

    @GetMapping("/qrcode-list")
    public HashMap<String, Object> getQrList(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("data", qrcodeService.getQrcodeByMember());
        return map;
    }
}
