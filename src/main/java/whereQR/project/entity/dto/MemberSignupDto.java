package whereQR.project.entity.dto;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberSignupDto {

    private String username;
    private String phoneNumber;
    private List<String> roles;

    private String password;

    public MemberSignupDto(){

    }

    public MemberSignupDto(String username,String phoneNumber, List<String> roles){
            this.username = username;
            this.phoneNumber = phoneNumber;
            this.roles = roles;
    }
}
