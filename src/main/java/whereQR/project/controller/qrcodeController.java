package whereQR.project.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

@RestController
@RequestMapping("/qrcode")
@Slf4j
public class qrcodeController {

    @PostMapping("/makeQR")
    public Object makeQr() throws WriterException {
        //qrcode를 생성할 때는 모두 null값으로 설정한다.
        int width = 200;
        int height = 200;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String key = timestamp.toString();
        log.info("makeQr-qrcode생성-qrcodekey값", key);

        BitMatrix matrix = new MultiFormatWriter().encode(key, BarcodeFormat.QR_CODE, width, height);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
