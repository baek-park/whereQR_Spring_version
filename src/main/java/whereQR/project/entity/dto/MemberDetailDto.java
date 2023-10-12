package whereQR.project.entity.dto;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import whereQR.project.entity.Member;
import whereQR.project.entity.PhoneNumber;
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
