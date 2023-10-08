package whereQR.project.entity.dto;

import lombok.Data;
import lombok.Getter;
import whereQR.project.entity.*;

import javax.persistence.Embedded;
import java.util.UUID;

@Data
public class QrcodeScanDto {

    private String title;
    private String memo;
    private QrStatus qrStatus;

    @Embedded
    PhoneNumber phoneNumber;

    private String url;

    private Long memberId;

    public QrcodeScanDto(Qrcode qrcode) {
        this.title = qrcode.getTitle();
        this.memo = qrcode.getMemo();
        this.phoneNumber = qrcode.getPhoneNumber();
        this.qrStatus = qrcode.getQrStatus();
        this.memberId = qrcode.getMember().getId();
        this.url = qrcode.getUrl();
    }

    public QrcodeScanDto(QrStatus qrStatus) {
       this.qrStatus = qrStatus;
    }
}
