package whereQR.project.domain.qrcode.dto;

import lombok.Data;
import whereQR.project.domain.qrcode.QrStatus;
import whereQR.project.domain.qrcode.Qrcode;

import java.util.UUID;

@Data
public class QrcodeScanDto {

    private String title;
    private String memo;
    private QrStatus qrStatus;

    private String phoneNumber;
    private String url;

    private UUID memberId;

    public QrcodeScanDto(Qrcode qrcode) {
        this.title = qrcode.getTitle();
        this.memo = qrcode.getMemo();
        this.phoneNumber = qrcode.getMember().getPhoneNumber();
        this.qrStatus = qrcode.getQrStatus();
        this.memberId = qrcode.getMember().getId();
        this.url = qrcode.getUrl();
    }

    public QrcodeScanDto(QrStatus qrStatus) {
       this.qrStatus = qrStatus;
    }
}
