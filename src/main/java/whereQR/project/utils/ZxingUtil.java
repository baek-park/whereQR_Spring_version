package whereQR.project.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import whereQR.project.properties.QrcodeProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

import static whereQR.project.domain.qrcode.QrcodeService.savePath;

@Slf4j
@RequiredArgsConstructor
public class ZxingUtil {
    private final QrcodeProperties qrcodeProperties;

    public HashMap<String ,Object> makeQrcodeMatrix(int width, int height) throws WriterException {
        UUID id = UUID.randomUUID();
        BitMatrix matrix = new MultiFormatWriter().encode(qrcodeProperties.getScanUrl() + id, BarcodeFormat.QR_CODE, width, height);
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        hashMap.put("matrix",matrix);
        return hashMap;
    }

    public static String imageToStringConverter(File qrImageFile) throws IOException {

        //image to byte -> encoding
        byte[] fileContent = FileUtils.readFileToByteArray(qrImageFile);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }

    public static File makeImageFile(UUID id, BitMatrix matrix) throws IOException {

        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
        File temp = new File(savePath + "/" + id + "qr" + ".png");
        ImageIO.write(bufferedImage, "png", temp);

        return temp;
    }
}
