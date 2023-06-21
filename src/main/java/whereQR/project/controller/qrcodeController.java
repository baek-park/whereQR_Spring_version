package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.QrcodeDetailDto;
import whereQR.project.entity.dto.QrcodeScanDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.service.QrcodeService;

import java.io.IOException;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Slf4j
public class qrcodeController {

    private final QrcodeService qrcodeService;

    @PostMapping("/create")
    public Qrcode makeQr() throws WriterException, IOException {
        return qrcodeService.makeQr();
    }

    @PostMapping("/update")
    public QrcodeUpdateDto updateQr(@RequestParam String key, @RequestBody QrcodeUpdateDto qrcodeUpdateDto) throws Exception {
        return qrcodeService.saveQr(key, qrcodeUpdateDto);
    }

    @GetMapping("/scan")
    public QrcodeScanDto getQr(@RequestParam String key){
        return qrcodeService.scanQr(key);
    }
}
