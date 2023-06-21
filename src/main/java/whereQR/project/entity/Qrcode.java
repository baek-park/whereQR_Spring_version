package whereQR.project.entity;

import lombok.Getter;
import whereQR.project.entity.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.Member;
import whereQR.project.entity.dto.QrcodeDetailDto;
import whereQR.project.entity.dto.QrcodeUpdateDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Qrcode {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String memo;
    private String qrcodeKey;
    private QrStatus qrStatus;

    @Embedded
    Address address;
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

    public Qrcode(String url, String key, QrStatus qrStatus){
        this.url = url;
        this.qrcodeKey = key;
        this.qrStatus = qrStatus;
    }

    //연관관계 편의 메서드Qrcode
    public void changeQrcode(Member member){
        this.member = member;
        member.getQrcodes().add(this);
    }

    public void updateQr(String title, String memo, QrStatus qrStatus, Member member ){
        this.title = title;
        this.memo = memo;
        this.createDate =  LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.qrStatus = qrStatus;
        this.member = member;
    }

    public void updateQr(String title, String memo,Address address,  PhoneNumber phoneNumber){
        this.title = title;
        this.memo = memo;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.updateDate = LocalDateTime.now();
    }

}
