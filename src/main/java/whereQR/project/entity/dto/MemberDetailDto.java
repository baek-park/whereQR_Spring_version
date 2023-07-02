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
    private int age;
    private Address address;
    private PhoneNumber phoneNumber;

    private List<Qrcode> qrcodes;

    public MemberDetailDto(String username, int age, List<Qrcode> qrcodes){
        this.username = username;
        this.age = age;
        this.qrcodes = qrcodes;
    }
}
