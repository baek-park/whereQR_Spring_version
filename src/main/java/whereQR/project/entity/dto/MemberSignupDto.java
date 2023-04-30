package whereQR.project.entity.dto;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import whereQR.project.entity.PhoneNumber;
import whereQR.project.entity.member.Member;

@Data
public class MemberSignupDto {

    private String username;
    private int age;
    private Address address;
    private PhoneNumber phoneNumber;

    private String password;
    private String email;

    public void MemberSignupDto(String username, int age, Address address , PhoneNumber phoneNumber){
        this.username = username;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    //builder pattern 사용
    public Member toMember(String username,int age, String email, String password){
        return Member.builder()
                .username(username)
                .age(age)
                .email(email)
                .password(password)
                .build();

    }

}
