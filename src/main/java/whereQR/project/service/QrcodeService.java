package whereQR.project.service;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.QrcodeRegisterDto;
import whereQR.project.entity.dto.QrcodeResponseDto;
import whereQR.project.entity.dto.QrcodeScanDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.entity.Qrcode;
import whereQR.project.properties.QrcodeProperties;
import whereQR.project.utils.GetUser;
import whereQR.project.utils.ZxingUtil;
import whereQR.project.repository.MemberRepository;
import whereQR.project.repository.QrcodeRepository;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static whereQR.project.entity.QrStatus.New;
import static whereQR.project.entity.QrStatus.Saved;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class QrcodeService {

    private final QrcodeRepository qrcodeRepository;
    private final MemberRepository memberRepository;
    private final QrcodeProperties qrcodeProperties;
    public static String savePath = "src/main/resources/static/qrcode";

    //관리자용 권한
    @Transactional
    public Qrcode createQrcode() throws WriterException {

        log.info("qrcodeProperties -> {}", qrcodeProperties);

        ZxingUtil zxingUtil = new ZxingUtil(qrcodeProperties);

        HashMap hashMap = zxingUtil.makeQrcodeMatrix(100,100);
        UUID id = (UUID) hashMap.get("id");
        BitMatrix matrix = (BitMatrix) hashMap.get("matrix");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            log.info("makeQR-qrcode-out/ out.toByteArray() => {}\n", out.toByteArray());

            //make image file
            File qrImageFile = ZxingUtil.makeImageFile(id, matrix);

            //convert image file to Base64 encoded string
            String qrImageUrl = ZxingUtil.imageToStringConverter(qrImageFile);
            log.info("makeQR-qrcode-out/ qrImageUrl => {}\n",qrImageUrl);

            Qrcode qrcode = new Qrcode(id,qrImageUrl, New);
            log.info("create qrcode id -> {}", qrcode.getId());

            return qrcodeRepository.save(qrcode);

        } catch (IOException e) {
            throw new RuntimeException(e); // Todo : 예외처리 변경
        }
    }


    /**
     *
     * @param id
     * @return QrcodeScanDto
     *
     * usage: qrcode를 스캔하는 서비스. 등록되지 않은 값이라면 scan이 불가능하고 -> 등록되지 않았다면 등록이 필요한 것.
     * (등록여부는 qrStatus로 구분.)
     */
    public QrcodeScanDto getQrcode(UUID id){

        Qrcode qrcode = qrcodeRepository.findById(id).orElseThrow(() -> new NotFoundException("해당하는 qrcode가 존재하지 않습니다.", this.getClass().toString()));
        switch (qrcode.getQrStatus()){
            case New:
                return new QrcodeScanDto(New);
            case Saved:
                return new QrcodeScanDto(qrcode);
        }

        return new QrcodeScanDto(qrcode);
    }

    /**
     *
     * @param id
     * @param qrcodeUpdateDto
     * @return
     *
     * 등록된 qrcode를 update
     *
     */
    @Transactional
    public QrcodeResponseDto updateQrcode(UUID id, QrcodeUpdateDto qrcodeUpdateDto){

        /**
         * 수정을 QrStatus로 구분
         */

        Member member = memberRepository.findMemberByUsername(GetUser.getUserName()).orElseThrow(() ->
                new NotFoundException("login이 필요합니다", this.getClass().toString()));

        Qrcode qrcode = qrcodeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("해당하는 qrcode가 존재하지 않습니다.", this.getClass().toString()));

        if( GetUser.getUserName().equals(qrcode.getMember().getUsername()) && qrcodeRepository.existsById(qrcode.getId())){
            qrcode.updateQrcode(qrcodeUpdateDto);
        }else{
            throw new ForbiddenException("접근 권한이 존재하지 않습니다", this.getClass().toString());
        }

        return qrcode.toQrcodeResponseDto();
    }

    /**
     * qrcode를 등록합니다.
     */
    @Transactional
    public QrcodeResponseDto registerQrcode(UUID id, QrcodeRegisterDto qrcodeRegisterDto){

        Member member = memberRepository.findMemberByUsername(GetUser.getUserName()).orElseThrow(() ->
                new NotFoundException("login이 필요합니다", this.getClass().toString()));

        Qrcode qrcode = qrcodeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("해당하는 qrcode가 존재하지 않습니다.", this.getClass().toString()));

        // 이미 등록된 qrcode는 등록할 수 없음
        if(qrcode.getQrStatus().equals(Saved)){
            throw new BadRequestException("이미 등록된 qrcode입니다.", this.getClass().toString());
        }

        qrcode.registerQrcode(qrcodeRegisterDto, Saved, member);

        return qrcode.toQrcodeResponseDto();

    }

    /**
     * user의 qrcodeList를 불러온다
     * @return
     */
    public List<QrcodeResponseDto> getQrcodeByMember(){

        String username = GetUser.getUserName();

        if(username.isEmpty() || !username.equals(GetUser.getUserName())){
            throw new ForbiddenException("접근 권한이 존재하지 않습니다.", this.getClass().toString());
        }

        Member member = memberRepository.findMemberByUsername(GetUser.getUserName()).orElseThrow(() -> new NotFoundException("login이 필요합니다", this.getClass().toString()));

        List<QrcodeResponseDto> qrcodeDetailDtoList = member.getQrcodeList().stream()
                .sorted(Comparator.comparing(Qrcode::getCreateDate).reversed())
                .map(it->it.toQrcodeResponseDto())
                .collect(Collectors.toList());

        return qrcodeDetailDtoList;
    }
}
