package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.qrcode.Qrcode;
import whereQR.project.service.qrcode.QrcodeService;

import java.io.IOException;

@RestController
@RequestMapping("/qrcode")
@RequiredArgsConstructor
@Slf4j
public class qrcodeController {

    private final QrcodeService qrcodeService;

    @PostMapping("/makeQR")
    public Qrcode makeQr() throws WriterException, IOException {
        return qrcodeService.make();
    }

}