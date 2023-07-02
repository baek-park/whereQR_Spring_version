package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.QrcodeScanDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.service.QrcodeService;

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
    public QrcodeUpdateDto updateQr(@RequestParam String key, @RequestBody QrcodeUpdateDto qrcodeUpdateDto){
        return qrcodeService.update(key, qrcodeUpdateDto);
    }

    @GetMapping("/scan")
    public QrcodeScanDto scanQr(@RequestParam String key){
        return qrcodeService.scan(key);
    }
}
