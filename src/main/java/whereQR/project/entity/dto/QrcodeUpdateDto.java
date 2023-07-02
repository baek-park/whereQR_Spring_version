package whereQR.project.entity.dto;

import lombok.Data;
import whereQR.project.entity.*;

import javax.persistence.Embedded;

@Data
public class QrcodeUpdateDto {

    private String title;
    private String memo;
    private QrStatus qrStatus;
    private Member member;

    @Embedded
    Address address;
    @Embedded
    PhoneNumber phoneNumber;

    public QrcodeUpdateDto(){

    }

    public QrcodeUpdateDto(String title, String memo){
        this.title = title;
        this.memo = memo;
    }

    public QrcodeUpdateDto(String title, String memo, Address address, PhoneNumber phoneNumber ){
        this.title = title;
        this.memo = memo;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public QrcodeUpdateDto(String title, String memo, Address address, PhoneNumber phoneNumber, QrStatus status, Member member){
        this.title = title;
        this.memo = memo;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.qrStatus = status;
        this.member = member;
    }

}


