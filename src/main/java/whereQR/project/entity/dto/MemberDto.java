package whereQR.project.entity.dto;

import lombok.Data;
import org.apache.tomcat.jni.Address;
import whereQR.project.entity.member.PhoneNumber;

public class MemberDto {

    private String username;
    private int age;
    private Address address;
    private PhoneNumber phoneNumber;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void MemberDto(String username, int age, Address address , PhoneNumber phoneNumber){
        this.username = username;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }


}
