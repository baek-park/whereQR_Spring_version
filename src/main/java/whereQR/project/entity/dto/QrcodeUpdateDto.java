package whereQR.project.entity.dto;

import lombok.Data;
import whereQR.project.entity.Address;
import whereQR.project.entity.Member;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.Qrcode;

import javax.persistence.Embedded;

@Data
public class QrcodeUpdateDto {

    private String title;
    private String memo;

    @Embedded
    Address address;
    @Embedded
    PhoneNumber phoneNumber;

    public QrcodeUpdateDto(String title, String memo, Address address, PhoneNumber phoneNumber){
        this.title = title;
        this.memo = memo;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

}


