package whereQR.project.entity.dto;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import whereQR.project.entity.Member;
import whereQR.project.entity.PhoneNumber;

@Data
public class MemberDetailDto {

    private String username;
    private int age;
    private Address address;
    private PhoneNumber phoneNumber;

    public MemberDetailDto(Member member){
        this.username = member.getUsername();
        this.age = member.getAge();
    }
}
