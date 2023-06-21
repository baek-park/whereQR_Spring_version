package whereQR.project.entity.dto;

import whereQR.project.entity.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.QrStatus;
import whereQR.project.entity.Qrcode;

import javax.persistence.Embedded;

public class QrcodeScanDto {

    private String title;
    private String memo;
    private QrStatus qrStatus;

    @Embedded
    Address address;
    @Embedded
    PhoneNumber phoneNumber;

    public QrcodeScanDto(Qrcode qrcode) {
        this.title = qrcode.getTitle();
        this.memo = qrcode.getMemo();
        this.address = qrcode.getAddress();
        this.phoneNumber = qrcode.getPhoneNumber();
        this.qrStatus = qrcode.getQrStatus();
    }

    public QrcodeScanDto(QrStatus qrStatus) {
       this.qrStatus = qrStatus;
    }
}
