package whereQR.project.service.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.Repository.QrcodeRepository;
import whereQR.project.entity.Qrcode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class QrcodeService {

    private final QrcodeRepository qrcodeRepository;
    public static String savePath = "src/main/resources/static/qrcode";

    public Qrcode make() throws WriterException, IOException {

        HashMap hashMap = makeQrcodeMatrix(200,200);
        String key = (String) hashMap.get("key");
        BitMatrix matrix = (BitMatrix) hashMap.get("matrix");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            log.info("makeQR-qrcode-out/ out.toByteArray() => {}\n", out.toByteArray());

            //make image file
            File qrImageFile = makeImageFile(key, matrix);

            //convert image file to Base64 encoded string
            String qrImageUrl = imageToStringConverter(qrImageFile);
            log.info("makeQR-qrcode-out/ qrImageUrl => {}\n",qrImageUrl);

            Qrcode qrcode = new Qrcode(qrImageUrl);
            return qrcodeRepository.save(qrcode);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<String ,Object> makeQrcodeMatrix(int width, int height) throws WriterException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String key = timestamp.toString();
        log.info("makeQr-qrcode생성-qrcodekey값/ key => {}", key);
        BitMatrix matrix = new MultiFormatWriter().encode(key, BarcodeFormat.QR_CODE, width, height);
        HashMap hashMap = new HashMap();
        hashMap.put("key",key);
        hashMap.put("matrix",matrix);
        return hashMap;
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
