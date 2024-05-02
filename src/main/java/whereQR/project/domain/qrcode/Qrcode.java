package whereQR.project.domain.qrcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import whereQR.project.domain.member.Member;
import whereQR.project.domain.qrcode.dto.QrcodeRegisterDto;
import whereQR.project.domain.qrcode.dto.QrcodeResponseDto;
import whereQR.project.domain.qrcode.dto.QrcodeUpdateDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
public class Qrcode {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String title;
    private String memo;
    private QrStatus qrStatus;
    private String phoneNumber;
    @Column(name="url", columnDefinition = "MEDIUMBLOB")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;


    public Qrcode() {

    }

    public Qrcode(UUID id,String url, QrStatus qrStatus){
        this.url = url;
        this.id = id;
        this.qrStatus = qrStatus;
    }

    //연관관계 편의 메서드Qrcode
    public void changeQrcode(Member member){
        this.member = member;
        member.getQrcodeList().add(this);
    }

    public void registerQrcode(QrcodeRegisterDto qrcodeRegisterDto, QrStatus qrStatus, Member member ){
        this.title = qrcodeRegisterDto.getTitle();
        this.memo = qrcodeRegisterDto.getMemo();
        this.createDate =  LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.qrStatus = qrStatus;
        this.member = member;
        this.phoneNumber = member.getPhoneNumber();
    }

    public void updateQrcode(QrcodeUpdateDto qrcodeUpdateDto){
        this.title = qrcodeUpdateDto.getTitle();
        this.memo = qrcodeUpdateDto.getMemo();
        this.phoneNumber = qrcodeUpdateDto.getPhoneNumber();
        this.updateDate = LocalDateTime.now();
    }

    public QrcodeResponseDto toQrcodeResponseDto(){
        return new QrcodeResponseDto(this);
    }

}
