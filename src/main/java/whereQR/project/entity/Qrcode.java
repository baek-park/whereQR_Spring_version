package whereQR.project.entity;

import lombok.Getter;
import whereQR.project.entity.dto.QrcodeRegisterDto;
import whereQR.project.entity.dto.QrcodeResponseDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;

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
    @Embedded
    PhoneNumber phoneNumber;
    @Column(name="url", columnDefinition = "MEDIUMBLOB")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime createDate;
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
        member.getQrcodes().add(this);
    }

    public void registerQrcode(QrcodeRegisterDto qrcodeRegisterDto, QrStatus qrStatus, Member member ){
        this.title = qrcodeRegisterDto.getTitle();
        this.memo = qrcodeRegisterDto.getMemo();
        this.createDate =  LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.qrStatus = qrStatus;
        this.member = member;
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
