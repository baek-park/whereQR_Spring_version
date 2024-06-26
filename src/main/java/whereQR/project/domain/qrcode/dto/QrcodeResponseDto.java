package whereQR.project.domain.qrcode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import whereQR.project.domain.qrcode.Qrcode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class QrcodeResponseDto {

    private UUID id;
    private String title;
    private String memo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    private String phoneNumber;

    private String url;

    private UUID memberId;

    public QrcodeResponseDto(){

    }

    public QrcodeResponseDto(Qrcode qrcode){
        this.id = qrcode.getId();
        this.title = qrcode.getTitle();
        this.memo = qrcode.getMemo();
        this.phoneNumber = qrcode.getPhoneNumber();
        this.createDate = qrcode.getCreateDate();
        this.updateDate = qrcode.getUpdateDate();
        this.memberId = qrcode.getMember().getId();
        this.url = qrcode.getUrl();
    }
}
