package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.QrcodeRegisterDto;
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
        return qrcodeService.make();
    }

    @GetMapping("/register")
    public QrcodeRegisterDto register(@RequestParam Long id, @RequestParam QrcodeRegisterDto qrcodeRegisterDto){
        return qrcodeService.register(id, qrcodeRegisterDto);
    }
}
