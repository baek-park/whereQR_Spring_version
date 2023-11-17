package whereQR.project.entity.dto.member;

import lombok.Data;
import whereQR.project.entity.Qrcode;

import java.util.List;

@Data
public class MemberDetailDto {

    private String username;
    private String phoneNumber;
    private List<Qrcode> qrcodes;

    public MemberDetailDto(String username, String phoneNumber, List<Qrcode> qrcodes){
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.qrcodes = qrcodes;
    }
}
