package whereQR.project.entity.dto;

import lombok.Data;
import whereQR.project.entity.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.Qrcode;

import javax.persistence.Embedded;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class QrcodeResponseDto {

    private UUID id;
    private String title;
    private String memo;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Embedded
    private PhoneNumber phoneNumber;
    @Embedded
    private Address address;

    public QrcodeResponseDto(){

    }

    public QrcodeResponseDto(Qrcode qrcode){
        this.id = qrcode.getId();
        this.title = qrcode.getTitle();
        this.memo = qrcode.getMemo();
        this.phoneNumber = qrcode.getPhoneNumber();
        this.address  = qrcode.getAddress();
        this.createDate = qrcode.getCreateDate();
        this.updateDate = qrcode.getUpdateDate();
    }
}
