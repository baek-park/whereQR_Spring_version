package whereQR.project.entity.dto;

import lombok.Data;
import whereQR.project.entity.Qrcode;

import java.time.LocalDateTime;

@Data
public class QrcodeResponseDto {

    private String title;
    private String key;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public QrcodeResponseDto(){

    }

    public QrcodeResponseDto(Qrcode qrcode){
        this.title = qrcode.getTitle();
        this.key = qrcode.getQrcodeKey();
        this.createDate = qrcode.getCreateDate();
        this.updateDate = qrcode.getUpdateDate();
    }
}
