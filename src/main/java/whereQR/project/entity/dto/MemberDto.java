package whereQR.project.entity.dto;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import whereQR.project.entity.member.PhoneNumber;

@Data
public class MemberDto {

    private String username;
    private int age;
    private Address address;
    private PhoneNumber phoneNumber;

    public void MemberDto(String username, int age, Address address , PhoneNumber phoneNumber){
        this.username = username;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }


}
