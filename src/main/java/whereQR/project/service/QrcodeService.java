package whereQR.project.service;

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
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.QrcodeScanDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.repository.MemberRepository;
import whereQR.project.repository.QrcodeRepository;
import whereQR.project.entity.Qrcode;
import whereQR.project.utils.GetUser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;

import static whereQR.project.entity.QrStatus.New;
import static whereQR.project.entity.QrStatus.Saved;

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
    public Qrcode makeQr() throws WriterException {

        HashMap hashMap = makeQrcodeMatrix(200,200);
        String key = (String) hashMap.get("key");
        BitMatrix matrix = (BitMatrix) hashMap.get("matrix");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            log.info("makeQR-qrcode-out/ out.toByteArray() => {}\n", out.toByteArray());

            //make image file
            File qrImageFile = makeImageFile(key, matrix);

            //convert image file to Base64 encoded string
            String qrImageUrl = imageToStringConverter(qrImageFile);
            log.info("makeQR-qrcode-out/ qrImageUrl => {}\n",qrImageUrl);

            Qrcode qrcode = new Qrcode(qrImageUrl, key, New);

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

    public QrcodeScanDto scanQr(String key){
        //주운 사람들까지 qrcode에 등록된 key를 활용해서 누구나 접근 가능해야함
        //값이 존재하면 넘겨줘야하고 Status가 false면 status만 넘겨줌
        Qrcode qrcode = qrcodeRepository.findQrcodeByQrcodeKey(key).orElseThrow(() -> new NotFoundException("key가 존재하지 않습니다.", this.getClass().toString()));
        switch (qrcode.getQrStatus()){
            case New:
                return new QrcodeScanDto(New);
            case Saved:
                return new QrcodeScanDto(qrcode);
        }

        return new QrcodeScanDto(qrcode);
    }

    @Transactional
    public QrcodeUpdateDto saveQr(String key, QrcodeUpdateDto qrcodeUpdateDto){

        /**
         * saveQr에서 최초등록과 수정을 QrStatus로 구분
         */

        Member member = memberRepository.findMemberByUsername(GetUser.getUserName()).orElseThrow(() -> new NotFoundException("login이 필요합니다", this.getClass().toString()));

        Qrcode qrcode = qrcodeRepository.findQrcodeByQrcodeKey(key).orElseThrow(() -> new NotFoundException("key가 존재하지 않습니다.", this.getClass().toString()));
        switch (qrcode.getQrStatus()){
            case New://최초로 등록하는 경우
                qrcode.updateQr(qrcodeUpdateDto.getTitle(), qrcodeUpdateDto.getMemo(), Saved, member);
                break;
            case Saved://이미 등록된 경우

                //이미 등록된 경우에는 동일한 고객이 아니라면 update가 불가능하다.
                if( GetUser.getUserName().equals(qrcode.getMember().getUsername()) ){
                    qrcode.updateQr(qrcodeUpdateDto.getTitle(), qrcodeUpdateDto.getMemo(), qrcodeUpdateDto.getAddress(), qrcodeUpdateDto.getPhoneNumber());
                    break;
                }else{
                    throw new ForbiddenException("접근 권한이 존재하지 않습니다", this.getClass().toString());
                }
        }
        return qrcode.toQrCodeUpdateDto();
    }
}
