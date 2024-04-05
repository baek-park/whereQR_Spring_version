package whereQR.project.service;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.qrcode.QrcodeRegisterDto;
import whereQR.project.entity.dto.qrcode.QrcodeResponseDto;
import whereQR.project.entity.dto.qrcode.QrcodeScanDto;
import whereQR.project.entity.dto.qrcode.QrcodeUpdateDto;
import whereQR.project.exception.CustomExceptions.BadRequestException;
import whereQR.project.exception.CustomExceptions.ForbiddenException;
import whereQR.project.exception.CustomExceptions.InternalException;
import whereQR.project.exception.CustomExceptions.NotFoundException;
import whereQR.project.entity.Qrcode;
import whereQR.project.properties.QrcodeProperties;
import whereQR.project.utils.MemberUtil;
import whereQR.project.utils.ZxingUtil;
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
    private final MemberService memberService;
    private final QrcodeProperties qrcodeProperties;
    public static String savePath = "src/main/resources/static/qrcode";

    //관리자용 권한
    @Transactional
    public Qrcode createQrcode() throws WriterException {

        ZxingUtil zxingUtil = new ZxingUtil(qrcodeProperties);

        HashMap hashMap = zxingUtil.makeQrcodeMatrix(100,100);
        UUID id = (UUID) hashMap.get("id");
        BitMatrix matrix = (BitMatrix) hashMap.get("matrix");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);

            //make image file
            File qrImageFile = ZxingUtil.makeImageFile(id, matrix);

            //convert image file to Base64 encoded string
            String qrImageUrl = ZxingUtil.imageToStringConverter(qrImageFile);
            Qrcode qrcode = new Qrcode(id,qrImageUrl, New);

            return qrcodeRepository.save(qrcode);

        } catch (IOException e) {
            throw new InternalException(e.getMessage(),this.getClass().toString());
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
     * 등록된 qrcode를 update
     */
    @Transactional
    public QrcodeResponseDto updateQrcodeByMember(UUID id, UUID memberId, QrcodeUpdateDto qrcodeUpdateDto){

        Member member = memberService.getMemberById(memberId);

        if(!qrcodeUpdateDto.validationPhoneNumber()){
            throw new BadRequestException("전화번호가 유효하지 않습니다.",this.getClass().toString());
        }

        Qrcode qrcode = qrcodeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("해당하는 qrcode가 존재하지 않습니다.", this.getClass().toString()));

        if( qrcode.getMember().equals(member) && qrcodeRepository.existsById(qrcode.getId())){
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

        Member currentMember = MemberUtil.getMember();

        Qrcode qrcode = qrcodeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("해당하는 qrcode가 존재하지 않습니다.", this.getClass().toString()));

        // 이미 등록된 qrcode는 등록할 수 없음
        if(qrcode.getQrStatus().equals(Saved)){
            throw new BadRequestException("이미 등록된 qrcode입니다.", this.getClass().toString());
        }

        qrcode.registerQrcode(qrcodeRegisterDto, Saved, currentMember);

        return qrcode.toQrcodeResponseDto();

    }

    /**
     * user의 qrcodeList를 불러온다
     * @return
     */
    public List<QrcodeResponseDto> getQrcodeByMember(UUID memberId){
        Member member = memberService.getMemberById(memberId);
        return member.getQrcodeList().stream()
                .sorted(Comparator.comparing(Qrcode::getCreateDate).reversed())
                .map(it->it.toQrcodeResponseDto())
                .collect(Collectors.toList());
    }
}
