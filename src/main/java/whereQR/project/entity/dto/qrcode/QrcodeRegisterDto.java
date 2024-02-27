package whereQR.project.entity.dto.qrcode;

import lombok.Data;

import java.util.UUID;

@Data
public class QrcodeRegisterDto {

    private UUID id;
    private String title;
    private String memo;

    public QrcodeRegisterDto(){

    }
}
