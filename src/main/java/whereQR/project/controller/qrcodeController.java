package whereQR.project.controller;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;
import whereQR.project.entity.dto.QrcodeDetailDto;
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
    public QrcodeUpdateDto updateQr(@RequestParam Long id, @RequestBody QrcodeUpdateDto qrcodeUpdateDto) throws Exception {
        return qrcodeService.updateQr(id, qrcodeUpdateDto);
    }

    @GetMapping("/detail")
    public QrcodeDetailDto getQr(@RequestParam Long id){
        return qrcodeService.getQr(id);
    }
}
