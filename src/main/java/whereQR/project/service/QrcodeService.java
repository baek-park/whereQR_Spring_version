package whereQR.project.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.QrcodeDetailDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.repository.MemberRepository;
import whereQR.project.repository.QrcodeRepository;
import whereQR.project.entity.Qrcode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class QrcodeService {

    private final QrcodeRepository qrcodeRepository;
    private final MemberRepository memberRepository;
    public static String savePath = "src/main/resources/static/qrcode";

    //관리자용 권한
    @Transactional
    public Qrcode makeQr() throws WriterException, IOException {

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

    public QrcodeDetailDto getQr(Long id){
        //주운 사람들까지 누구나 접근 가능해야함

        Qrcode qrcode = qrcodeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id is null"));
        return new QrcodeDetailDto(qrcode);
    }

    // 기존에 관리자가 만들어준 QRcode에서 사용자가 속성을 입력하여 update
    @Transactional
    public QrcodeUpdateDto updateQr(Long id, QrcodeUpdateDto qrcodeUpdateDto) throws Exception {

        /**
         * To do : 해당 유저에 대한 큐알인지 체크
         * querydsl을 활용해서 해당 qrcode의 context 정보안의 member의 username이 해당 username인지 확인
         * 혹은 updateTime이 null일 경우 -> 새로 등록하는 경우여서 가능하도록 해야함
         */

        if( !qrcodeRepository.existsById(id) ){
            log.error("존재하지 않는 QRcode ID입니다."+ id);
            throw new IllegalArgumentException("찾을 수 없는 id");
        }

        //update
        Optional<Qrcode> qrcode = qrcodeRepository.findById(id);
        qrcode.get().update(qrcodeUpdateDto);
        return qrcodeUpdateDto;
    }

}
