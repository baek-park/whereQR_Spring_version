package whereQR.project.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whereQR.project.entity.Qrcode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

@RestController
@RequestMapping("/qrcode")
@Slf4j
public class qrcodeController {

    public static String savePath = "src/main/resources/static/qrcode";

    @PostMapping("/makeQR")
    public Object makeQr() throws WriterException, IOException {

        //qrcode를 생성할 때는 모두 null값으로 설정한다.
        int width = 200;
        int height = 200;

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String key = timestamp.toString();
        log.info("makeQr-qrcode생성-qrcodekey값/ key => {}", key);

        BitMatrix matrix = new MultiFormatWriter().encode(key, BarcodeFormat.QR_CODE, width, height);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            log.info("makeQR-qrcode-out/ out.toByteArray() => {}\n", out.toByteArray());

            //make image file
            File qrImageFile = makeImageFile(key, matrix);

            //convert image file to Base64 encoded string
            String qrImageUrl = imageToStringConverter(qrImageFile);
            log.info("makeQR-qrcode-out/ qrImageUrl => {}\n",qrImageUrl);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String imageToStringConverter(File qrImageFile) throws IOException {

        //image to byte -> encoding
        byte[] fileContent = FileUtils.readFileToByteArray(qrImageFile);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }

    private File makeImageFile(String key, BitMatrix matrix) throws IOException {

        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        File temp = new File(savePath + "/" + key + "qr" + ".png");
        ImageIO.write(bufferedImage, "png", temp);

        return temp;
    }

}
